#pragma once

#undef PI
#define PI 3.14159265358979323846264338327950288419716939937510582097494459230781

#define IOR_AIR 1
#define IOR_WATER 1.333
#define IOR_WATER_TO_AIR (IOR_WATER / IOR_AIR)
#define IOR_AIR_TO_WATER (IOR_AIR / IOR_WATER)

#define RENDER_PASS_MAIN 0
#define RENDER_PASS_WATER_REFLECTION 1

// Any changes here may need to be reflected in OpenCL's constants.cl
// They are kept separate to avoid accidentally breaking OpenCL compatibility
#define MATERIAL_INDEX_SHIFT 12
#define MATERIAL_SHADOW_OPACITY_THRESHOLD_SHIFT 6
#define MATERIAL_FLAG_DISABLE_SHADOW_RECEIVING 5
#define MATERIAL_FLAG_UPWARDS_NORMALS 4
#define MATERIAL_FLAG_FLAT_NORMALS 3
#define MATERIAL_FLAG_WORLD_UVS 2
#define MATERIAL_FLAG_VANILLA_UVS 1
#define MATERIAL_FLAG_IS_OVERLAY 0

#include SHADOW_MODE
#define SHADOW_MODE_OFF 0
#define SHADOW_MODE_FAST 1
#define SHADOW_MODE_DETAILED 2

#define SHADOW_DEPTH_BITS 16
#define SHADOW_ALPHA_BITS 8
#define SHADOW_COMBINED_BITS (SHADOW_DEPTH_BITS + SHADOW_ALPHA_BITS)
#define SHADOW_DEPTH_MAX ((1 << SHADOW_DEPTH_BITS) - 1)
#define SHADOW_ALPHA_MAX ((1 << SHADOW_ALPHA_BITS) - 1)
#define SHADOW_COMBINED_MAX ((1 << SHADOW_COMBINED_BITS) - 1)

#include SHADOW_TRANSPARENCY
#if SHADOW_TRANSPARENCY
#define SHADOW_DEFAULT_OPACITY_THRESHOLD 0.01 // Remove shadows from clickboxes
#else
#define SHADOW_DEFAULT_OPACITY_THRESHOLD 0.71 // Lowest while keeping Prifddinas glass walkways transparent
#endif

#include VANILLA_COLOR_BANDING
#include UNDO_VANILLA_SHADING
#include LEGACY_GREY_COLORS
#include LEGACY_WATER
#include LINEAR_ALPHA_BLENDING
#include DISABLE_DIRECTIONAL_SHADING
#include FLAT_SHADING
#include SHADOW_MAP_OVERLAY
#include APPLY_COLOR_FILTER
#include WATER_FOAM
#include PLANAR_REFLECTIONS