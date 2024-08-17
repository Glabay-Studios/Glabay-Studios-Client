/*
 * Copyright (c) 2024, Hooder <ahooder@protonmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
#include utils/constants.glsl
#include utils/color_utils.glsl
#include utils/shadows.glsl
#include utils/texture_bicubic.glsl

// Index of refraction and the cosine of the angle between the normal vector and a vector towards the camera
float calculateFresnel(const float cosi, const float iorFrom, const float iorTo) {
    float R0 = (iorFrom - iorTo) / (iorFrom + iorTo);
    R0 *= R0;
    return R0 + (1 - R0) * pow(1 - cosi, 5);
}

// Air to medium by default
float calculateFresnel(const float cosi, const float ior) {
    return calculateFresnel(cosi, IOR_AIR, ior);
}

// Air to medium by default
float calculateFresnel(const vec3 I, const vec3 N, const float ior) {
    return calculateFresnel(dot(I, N), ior);
}

// Fresnel approximation which accounts for total internal reflection
// Normalized incident and normal vectors, and index of refraction
float calculateFresnelTIR(const vec3 I, const vec3 N, const float ior) {
    float R0 = (ior - 1) / (ior + 1);
    R0 *= R0;
    float cosi = dot(I, N);
    if (ior < 1) {
        // Moving from denser to lighter medium
        float inv_eta = 1 / ior;
        float sintSquared = inv_eta * inv_eta * (1 - cosi * cosi);
        if (sintSquared > 1)
            return 1; // total internal reflection
        cosi = sqrt(1 - sintSquared);
    }
    return R0 + (1 - R0) * pow(1 - cosi, 5);
}

void sampleUnderwater(inout vec3 outputColor, int waterTypeIndex, float depth) {
    WaterType waterType = getWaterType(waterTypeIndex);

    // Make the color appear like wet sand to start off with
    outputColor *= vec3(2.5, 3.5, 3.5);

    // The idea is to approximate light absorption using 3 frequency bands.
    // To accomplish this, we must transform linear RGB into linear XYZ,
    // then into our chosen 3 frequency bands, do all of the light calculations,
    // then finally transform it back into linear RGB

    // Pure water based on https://en.wikipedia.org/wiki/Electromagnetic_absorption_by_water#/media/File:Absorption_coefficient_of_water.svg
    // Exponential extinction coefficient per meter
    vec3 extinctionCoefficients = vec3(
        0.2224, // ~red   600 nm
        0.0565, // ~green 550 nm
        0.00922 // ~blue  450 nm
    );
    // Approximate some kind of ocean water with phytoplankton absorption based on https://www.oceanopticsbook.info/view/absorption/absorption-by-oceanic-constituents
//    extinctionCoefficients += vec3(
//        0.0275,
//        0.0175,
//        0.005
//    );

    // Attempt #1 at picking some numbers which look nice
    extinctionCoefficients = vec3(
        .1,
        .1,
        .00922
    );

    // Convert extinction coefficients to in-game units
    extinctionCoefficients /= 128.f;

    // Convert to XYZ at the end https://en.wikipedia.org/wiki/CIE_1931_color_space#Color_matching_functions
    mat3 bandsToXyz = transpose(mat3(
        1.062200000000, 0.631000000000, 0.000800000000, // 600 nm
        0.433449900000, 0.994950100000, 0.008749999000, // 550 nm
        0.336200000000, 0.038000000000, 1.772110000000  // 450 nm
    ));
    mat3 xyzToBands = inverse(bandsToXyz);

    float absorptionAdjustment = exp(5 * (1 - waterTransparencyAmount));
    extinctionCoefficients *= absorptionAdjustment;

    // Refraction is not precalculated for the underwater position
    vec3 fragPos = IN.position;
    vec3 underwaterNormal = normalize(IN.normal);
    const vec3 surfaceNormal = vec3(0, -1, 0); // Assume a flat surface

    vec3 sunDir = -lightDir; // the light's direction from the sun towards any fragment
    vec3 refractedSunDir = refract(sunDir, surfaceNormal, IOR_AIR_TO_WATER);
    float sunToFragDist = depth / refractedSunDir.y;

    vec3 camToFrag = normalize(fragPos - cameraPos);
    // We ignore refraction effects on the way back up to the surface
    // TODO: support viewing underwater geometry from below in waterfalls properly
    float fragToSurfaceDist = abs(depth / camToFrag.y);

    // Attenuate directional and ambient light by their distances travelled on the way down
    vec3 directionalAttenuation = exp(-extinctionCoefficients * sunToFragDist);
    vec3 ambientAttenuation = exp(-extinctionCoefficients * depth);
    // Attenuate the light on the way back up to the surface
    vec3 upwardAttenuation = exp(-extinctionCoefficients * fragToSurfaceDist);

    // Initialize with linear RGB colors
    vec3 directionalLight = lightColor * lightStrength;
    vec3 ambientLight = ambientColor * ambientStrength;

    // Add underwater caustics as additional directional light
    if (underwaterCaustics) {
        vec2 causticsUv = worldUvs(2.5);
        const vec2 direction = vec2(1, -2);
        vec2 flow1 = causticsUv + animationFrame(13) * direction;
        vec2 flow2 = causticsUv * 1.5 + animationFrame(17) * -direction;
        // Chromatic abberation makes it appear like caustics are adding shadows, so don't use it
        // vec3 caustics = sampleCaustics(flow1, flow2, depth * .00001);
        vec3 caustics = sampleCaustics(flow1, flow2);

        // Apply caustics color based on the environment
        // Usually this falls back to directional lighting
        caustics *= underwaterCausticsColor * underwaterCausticsStrength;

        // Fade caustics out too close to the shoreline
        caustics *= min(1, smoothstep(0, 1, depth / 32));

        // Fade caustics out with depth, since they should decay sharply due to focus
        caustics *= max(0, 1 - smoothstep(0, 1, depth / 768));

        // Artificially boost strength
        caustics *= 15;

        directionalLight += caustics;
    }

    // Disable shadows for flat water, as it needs more work
    if (waterTransparency && !waterType.isFlat) {
        // For shadows, we can take refraction into account, since sunlight is parallel
        vec3 surfaceSunPos = fragPos - refractedSunDir * sunToFragDist;
        surfaceSunPos += refractedSunDir * 32; // Push the position a short distance below the surface
        vec2 distortion = vec2(0);
        {
            vec2 flowMapUv = worldUvs(15) + animationFrame(50 * waterType.duration);
            float flowMapStrength = 0.025;
            vec2 uvFlow = textureBicubic(textureArray, vec3(flowMapUv, waterType.flowMap)).xy;
            distortion = uvFlow * .0015 * (1 - exp(-depth));
        }
        float shadow = sampleShadowMap(surfaceSunPos, distortion, dot(-sunDir, underwaterNormal));
        // Attenuate directional by shadowing
        directionalLight *= 1 - shadow;
    }

    // Attenuate directional light by fresnel refraction
    directionalLight *= 1 - calculateFresnel(max(0, dot(-sunDir, surfaceNormal)), IOR_WATER);
    // Attenuate ambient light by fresnel refraction travelling straight down
    ambientLight *= 1 - calculateFresnel(1, IOR_WATER);

    // Attenuate based on the direction light hits the underwater surface
    directionalLight *= max(0, dot(-refractedSunDir, underwaterNormal));
    // This is a bit questionable, since ambient comes from all directions, but here we assume it goes down
    ambientLight *= -underwaterNormal.y;

    // Scale colors to safe ranges for conversion to XYZ
    float intensityFactor = max(
        max(max(directionalLight.r, directionalLight.g), directionalLight.b),
        max(max(ambientLight.r, ambientLight.g), ambientLight.b)
    );
    directionalLight /= intensityFactor;
    ambientLight /= intensityFactor;

    // Convert light into frequency bands for extinction calculations
    directionalLight = xyzToBands * RGBtoXYZ(directionalLight);
    ambientLight = xyzToBands * RGBtoXYZ(ambientLight);

    // Attenuate light on the way down towards the underwater surface
    directionalLight *= directionalAttenuation;
    ambientLight *= ambientAttenuation;

    // Combine both sources of light upon reaching the fragment
    vec3 light = ambientLight + directionalLight;

    // Attenuate light on the way back up towards the water surface
    light *= upwardAttenuation;

    // Convert the light back into RGB
    light = XYZtoRGB(bandsToXyz * light);

    // Bring the intensity back
    light *= intensityFactor;

    // Light doesn't need to be attenuated on the way back up through the surface, since the surface
    // already handles this with alpha blending. The only remaining issue might be accounting for
    // total internal reflection, which can maybe also be done by the surface, where normals are known

    // Apply the calculated light to the fragment's color
    outputColor *= light;
}

vec4 sampleWater(int waterTypeIndex, vec3 viewDir) {
    WaterType waterType = getWaterType(waterTypeIndex);

    const float scale = 1;
    float waveHeight = 1;
    float speed = .1 / sqrt(scale);

    waveHeight *= waterWaveSize;
    speed *= waterWaveSpeed;

    vec2 uv1 = worldUvs(26 * scale) + animationFrame(sqrt(26. * scale) / speed * waterType.duration);
    vec2 uv2 = worldUvs(6 * scale) + animationFrame(sqrt(6. * scale) / speed * waterType.duration) * vec2(-2, 3);

    // get diffuse textures
//    vec3 n1 = linearToSrgb(texture(textureArray, vec3(uv1, MAT_WATER_NORMAL_MAP_1.colorMap)).xyz);
    vec3 n2 = linearToSrgb(texture(textureArray, vec3(uv2, MAT_WATER_NORMAL_MAP_2.colorMap)).xyz);
    vec3 n1 = linearToSrgb(textureBicubic(textureArray, vec3(uv1, MAT_WATER_NORMAL_MAP_1.colorMap)).xyz);
//    vec3 n2 = linearToSrgb(textureBicubic(textureArray, vec3(uv2, MAT_WATER_NORMAL_MAP_2.colorMap)).xyz);

    // Normalize
    n1.xy = (n1.xy * 2 - 1);
    n2.xy = (n2.xy * 2 - 1);
    // Tangent space to world
    n1.z *= -1;
    n2.z *= -1;
    n1.xyz = n1.xzy;
    n2.xyz = n2.xzy;
    n1 = normalize(vec3(1, 1 / waveHeight, 1) * n1);
    n2 = normalize(vec3(1, 1 / waveHeight, 1) * n2);
    vec3 N = normalize(n1 + n2);

    vec3 fragToCam = viewDir;
    vec3 I = -viewDir; // incident

    // Assume the water is level
    vec3 flatR = reflect(I, vec3(0, -1, 0));
    vec3 R = reflect(I, N);
    float distortionFactor = 20;

    // Initialize the reflection with a fake sky reflection
    vec4 reflection = vec4(
        sampleWaterReflection(flatR, R, distortionFactor),
        calculateFresnel(dot(fragToCam, N), IOR_WATER)
    );

    vec3 additionalLight = vec3(0);

    // Scattering approximation
//    const float k_1 = 0; // This doesn't work for our normal map-based waves unfortunately
    const float k_2 = .01;
    const float k_3 = .02;
    const float k_4 = 0;
    const vec3 C_ss = vec3(0, .32, .32); // water scatter color
    const vec3 C_f = vec3(1); // air bubble color
    const float P_f = .01; // density of air bubbles

//    float H = (1 + N.y) * 50; // wave height
    vec3 omega_i = lightDir; // incoming = sun to frag
    vec3 omega_o = viewDir; // outgoing = frag to camera
    vec3 omega_h = normalize(omega_o - omega_i); // half-way between incoming and outgoing
    vec3 omega_n = IN.normal.xzy; // macro scale normal
    vec3 w_n = N; // presumably wave normal?

    vec3 L_sun = lightColor * lightStrength;
    vec3 L_scatter = (
//        k_1*H*pow(max(0, dot(omega_i, -omega_o)), 4.f) * pow(.5 - .5*dot(omega_i, omega_n), 3.f)
        + k_2*pow(max(0, dot(omega_o, omega_n)), 2.f)
    ) * C_ss*L_sun;
    L_scatter += k_3*max(0, dot(omega_i, w_n))*C_ss*L_sun + k_4*P_f*C_f*L_sun;
    additionalLight += L_scatter;

    float specularGloss = waterType.specularGloss;
    float specularStrength = waterType.specularStrength;
    vec3 sunSpecular = pow(max(0, dot(R, lightDir)), specularGloss) * lightStrength * lightColor * specularStrength;
    additionalLight += sunSpecular;

    // Point lights
    vec3 pointLightsSpecular = vec3(0);
    for (int i = 0; i < pointLightsCount; i++) {
        vec4 pos = PointLightArray[i].position;
        vec3 lightToFrag = pos.xyz - IN.position;
        float distanceSquared = dot(lightToFrag, lightToFrag);
        float radiusSquared = pos.w;
        // TODO: decide whether we want to restrict this. It doesn't really make sense to, but might help with performance
        // if (distanceSquared <= radiusSquared) {
            vec3 pointLightColor = PointLightArray[i].color;
            vec3 pointLightDir = normalize(lightToFrag);

            float attenuation = 1 - min(distanceSquared / radiusSquared, 1);
            pointLightColor *= attenuation * attenuation;

            vec3 pointLightReflectDir = reflect(-pointLightDir, N);
            pointLightsSpecular += pointLightColor * pow(max(0, dot(pointLightReflectDir, viewDir)), specularGloss) * specularStrength;
        // }
    }
    additionalLight += pointLightsSpecular;

    // Begin constructing final output color
    vec4 dst = reflection;

    // In theory, we could just add the light and be done with it, but since the color
    // will be multiplied by alpha during alpha blending, we need to divide by alpha to
    // end up with our target amount of additional light after alpha blending
    dst.rgb += additionalLight / dst.a;

    // The issue now is that or color may exceed 100% brightness, and get clipped.
    // To work around this, we can adjust the alpha component to let more of the light through,
    // and adjust our color accordingly. This necessarily causes the surface to become more opaque,
    // but since we're adding lots of light, this should have minimal impact on the final picture.
    float maxIntensity = max(max(dst.r, dst.g), dst.b);
    // Check if the color would get clipped
    if (maxIntensity > 1) {
        // Bring the brightest color back down to 1
        dst.rgb /= maxIntensity;
        // And bump up the alpha to increase brightness instead
        dst.a *= maxIntensity;
        // While not strictly necessary, we might as well clamp the alpha component in case it exceeds 1
        dst.a = min(1, dst.a);
    }

    if (waterTypeIndex == 7) {
        if (dot(reflection.rgb, reflection.rgb) == 0) {
            dst.rgb = srgbToLinear(vec3(100, 0, 0) / 255.f) * 2.5;
        }
    }

    // If the water is opaque, blend in a fake underwater surface
    if (waterType.isFlat || !waterTransparency) {
        // Computed from packedHslToSrgb(6676)
        const vec3 underwaterColor = vec3(0.04856183, 0.025971446, 0.005794384);
        const int depth = 600;

        vec4 src = dst;
        dst.rgb = underwaterColor;
        sampleUnderwater(dst.rgb, waterTypeIndex, depth);

        dst.rgb = mix(dst.rgb, src.rgb, src.a);
        dst.a = 1;
    }

    return dst;
}
