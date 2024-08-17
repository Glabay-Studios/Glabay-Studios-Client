package com.client.engine;

import com.client.Bounds;
import com.client.Client;
import com.client.ProducingGraphicsBuffer;
import com.client.audio.StaticSound;
import com.client.engine.impl.KeyHandler;
import com.client.engine.impl.MouseHandler;
import com.client.engine.impl.MouseWheel;
import com.client.engine.impl.MouseWheelHandler;
import com.client.engine.task.Clock;
import com.client.engine.task.TaskHandler;
import com.client.engine.task.TaskUtils;
import com.client.engine.task.impl.MilliClock;
import com.client.engine.task.impl.NanoClock;
import com.client.sign.Signlink;
import com.client.util.MonotonicClock;
import net.runelite.api.events.CanvasSizeChanged;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.rs.api.RSGameEngine;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

public abstract class GameEngine extends Applet implements Runnable, WindowListener, RSGameEngine, FocusListener {

    MouseWheelHandler mouseWheelHandler;
    public static TaskHandler taskHandler;
    static GameEngine gameEngine;
    public static int canvasWidth;
    public static int canvasHeight;
    static int threadCount;
    static long stopTimeMs;
    static boolean isKilled;
    static int cycleDurationMillis;

    private Thread thread;

    @Override
    public Thread getClientThread() {
        return thread;
    }

    @Override
    public boolean isClientThread() {
        return thread == Thread.currentThread();
    }

    static int fiveOrOne;
    protected static int fps;
    public static long[] graphicsTickTimes;
    public static long[] clientTickTimes;
    static int field209;
    static int field199;
    static volatile boolean volatileFocus;
    static long garbageCollectorTime;
    static long lastGarbageCollect;

    boolean hasErrored;
    protected int contentWidth;
    protected int contentHeight;
    int canvasX;
    int canvasY;
    int maxCanvasWidth;
    int maxCanvasHeight;

    Frame frame;
    public Canvas canvas;

    volatile boolean fullRedraw;
    boolean resizeCanvasNextFrame;
    volatile boolean isCanvasInvalid;
    volatile long field185;
    final EventQueue eventQueue;

    private static final long serialVersionUID = 1L;
    private static int viewportColor;
    protected boolean hasDepth = false;
    public static KeyHandler keyHandler;
    public static int[] keyCodes;


    static {
        gameEngine = null;
        keyHandler = new KeyHandler();
        threadCount = 0;
        stopTimeMs = 0L;
        isKilled = false;
        cycleDurationMillis = 20;
        fiveOrOne = 1;
        fps = 0;
        graphicsTickTimes = new long[32];
        clientTickTimes = new long[32];
        field199 = 500;
        volatileFocus = true;
        garbageCollectorTime = -1L;
        lastGarbageCollect = -1L;
        keyCodes = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, 85, 80, 84, -1, 91, -1, -1, -1, 81, 82, 86, -1, -1, -1, -1, -1, -1, -1, -1, 13, -1, -1, -1, -1, 83, 104, 105, 103, 102, 96, 98, 97, 99, -1, -1, -1, -1, -1, -1, -1, 25, 16, 17, 18, 19, 20, 21, 22, 23, 24, -1, -1, -1, -1, -1, -1, -1, 48, 68, 66, 50, 34, 51, 52, 53, 39, 54, 55, 56, 70, 69, 40, 41, 32, 35, 49, 36, 38, 67, 33, 65, 37, 64, -1, -1, -1, -1, -1, 228, 231, 227, 233, 224, 219, 225, 230, 226, 232, 89, 87, -1, 88, 229, 90, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -1, -1, -1, 101, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 100, -1, 87, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    }

    protected GameEngine() {
        hasErrored = false;
        canvasX = 0;
        canvasY = 0;
        fullRedraw = true;
        resizeCanvasNextFrame = false;
        isCanvasInvalid = false;
        field185 = 0L;
        EventQueue queue = null;

        try {
            queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        eventQueue = queue;
        StaticSound.init();
    }


    protected final void setMaxCanvasSize(int width, int height) {
        if (Client.instance.isStretchedEnabled() && Client.instance.isResized()) {
            return;
        }

        if (maxCanvasWidth != width || height != maxCanvasHeight) {
            flagResize();
        }

        maxCanvasWidth = width;
        maxCanvasHeight = height;

    }

    public final void post(Object var1) {
        if (!Client.instance.isGpu()) {
            if (eventQueue != null) {
                for (int var2 = 0; var2 < 50 && eventQueue.peekEvent() != null; ++var2) {
                    TaskUtils.sleep(1L);
                }

                if (var1 != null) {
                    eventQueue.postEvent(new ActionEvent(var1, 1001, "dummy"));
                }

            }
        } else {
            DrawCallbacks drawCallbacks = Client.instance.getDrawCallbacks();
            if (drawCallbacks != null) {
                drawCallbacks.draw(viewportColor);
            }
        }
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    public final void resizeCanvas() {
        if (Client.instance.isStretchedEnabled()) {
            Client.instance.invalidateStretching(false);

            if (Client.instance.isResized()) {
                Dimension realDimensions = Client.instance.getRealDimensions();

                setMaxCanvasWidth(realDimensions.width);
                setMaxCanvasHeight(realDimensions.height);
            }
        }

        Container container = container();
        if (container != null) {
            Bounds contentBounds = getFrameContentBounds();
            contentWidth = Math.max(contentBounds.highX, 0);
            contentHeight = Math.max(contentBounds.highY, 0);
            if (contentWidth <= 0) {
                contentWidth = 1;
            }

            if (contentHeight <= 0) {
                contentHeight = 1;
            }

            if (Client.instance.isResized()) {
                setMaxCanvasSize(contentWidth, contentHeight);
            }

            canvasWidth = Math.min(contentWidth, maxCanvasWidth);
            canvasHeight = Math.min(contentHeight, maxCanvasHeight);
            Client.instance.getCallbacks().post(CanvasSizeChanged.INSTANCE);
            canvasX = (contentWidth - canvasWidth) / 2;
            canvasY = 0;
            canvas.setSize(canvasWidth, canvasHeight);
            Client.rasterProvider = new ProducingGraphicsBuffer(canvasWidth, canvasHeight, canvas, this.hasDepth);
            if (container == frame) {
                Insets insets = frame.getInsets();
                canvas.setLocation(canvasX + insets.left, insets.top + canvasY);
            } else {
                canvas.setLocation(canvasX, canvasY);
            }

            fullRedraw = true;
            resizeGame();
        }
    }

    protected abstract void resizeGame();

    void clearBackground() {
        int canvasX = this.canvasX;
        int canvasY = this.canvasY;
        int width = contentWidth - canvasWidth - canvasX;
        int height = contentHeight - canvasHeight - canvasY;
        if (canvasX > 0 || width > 0 || canvasY > 0 || height > 0) {
            try {
                Container container = container();
                int left = 0;
                int top = 0;
                if (container == frame) {
                    Insets var8 = frame.getInsets();
                    left = var8.left;
                    top = var8.top;
                }

                Graphics graphics = container.getGraphics();
                graphics.setColor(Color.black);
                if (canvasX > 0) {
                    graphics.fillRect(left, top, canvasX, contentHeight);
                }

                if (canvasY > 0) {
                    graphics.fillRect(left, top, contentWidth, canvasY);
                }

                if (width > 0) {
                    graphics.fillRect(left + contentWidth - width, top, width, contentHeight);
                }

                if (height > 0) {
                    graphics.fillRect(left, top + contentHeight - height, contentWidth, height);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public boolean isResizeCanvasNextFrame() {
        return resizeCanvasNextFrame;
    }

    @Override
    public void setResizeCanvasNextFrame(boolean resize) {
        resizeCanvasNextFrame = resize;
    }

    @Override
    public boolean isReplaceCanvasNextFrame() {
        return isCanvasInvalid;
    }

    @Override
    public void setReplaceCanvasNextFrame(boolean replace) {
        isCanvasInvalid = replace;
    }

    @Override
    public void setMaxCanvasWidth(int width) {
        maxCanvasWidth = width;
    }

    @Override
    public void setMaxCanvasHeight(int height) {
        maxCanvasHeight = height;
    }

    @Override
    public void setFullRedraw(boolean fullRedraw) {
        this.fullRedraw = fullRedraw;
    }


    final void replaceCanvas() {
        if (Client.instance != null && Client.instance.isGpu()) {
            setFullRedraw(false);
            return;
        }
        canvas.removeMouseListener(MouseHandler.instance);
        canvas.removeMouseMotionListener(MouseHandler.instance);
        canvas.removeFocusListener(MouseHandler.instance);
        MouseHandler.currentButton = 0;
        if (this.mouseWheelHandler != null) {
            this.mouseWheelHandler.removeFrom(this.canvas);
        }
        addCanvas();
        keyHandler.setupComponent(this.canvas);
        this.canvas.addMouseListener(MouseHandler.instance);
        this.canvas.addMouseMotionListener(MouseHandler.instance);
        this.canvas.addFocusListener(MouseHandler.instance);
        if (this.mouseWheelHandler != null) {
            this.mouseWheelHandler.addTo(this.canvas);
        }

        flagResize();
    }

    protected final void startThread(int var1, int var2, int var3, int var4) {
        try {
            if (gameEngine != null) {
                ++threadCount;
                if (threadCount >= 3) {
                    error("alreadyloaded");
                    return;
                }

                getAppletContext().showDocument(getDocumentBase(), "_self");
                return;
            }

            gameEngine = this;
            canvasWidth = var1;
            canvasHeight = var2;
            Client.instance.getCallbacks().post(CanvasSizeChanged.INSTANCE);
            if (taskHandler == null) {
                taskHandler = new TaskHandler();
            }
            taskHandler.newThreadTask(this, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            error("crash");
        }

    }

    final synchronized void addCanvas() {
        Container container = container();
        if (canvas != null) {
            canvas.removeFocusListener(this);
            container.remove(canvas);
        }

        canvasWidth = Math.max(container.getWidth(), 0);
        canvasHeight = Math.max(container.getHeight(), 0);
        Client.instance.getCallbacks().post(CanvasSizeChanged.INSTANCE);
        Insets insets;
        if (frame != null) {
            insets = frame.getInsets();
            canvasWidth -= insets.left + insets.right;
            canvasHeight -= insets.bottom + insets.top;
        }

        canvas = new Canvas(this);
        setupMouse();


        container.setBackground(Color.BLACK);
        container.setLayout(null);
        container.add(canvas);
        canvas.setSize(canvasWidth, canvasHeight);
        canvas.setVisible(true);
        canvas.setBackground(Color.BLACK);
        if (container == frame) {
            insets = frame.getInsets();
            canvas.setLocation(insets.left + canvasX, canvasY + insets.top);
        } else {
            canvas.setLocation(canvasX, canvasY);
        }

        canvas.addFocusListener(this);
        canvas.requestFocus();
        fullRedraw = true;
        if (Client.rasterProvider != null && canvasWidth == Client.rasterProvider.width && canvasHeight == Client.rasterProvider.height) {
            ((ProducingGraphicsBuffer) Client.rasterProvider).setComponent(canvas);
            Client.rasterProvider.drawFull(0, 0);
        } else {
            Client.rasterProvider = new ProducingGraphicsBuffer(canvasWidth, canvasHeight, canvas, this.hasDepth);

        }
        isCanvasInvalid = false;
        field185 = MonotonicClock.currentTimeMillis();
    }


    void clientTick() {
        long var1 = MonotonicClock.currentTimeMillis();
        clientTickTimes[field209] = var1;
        field209 = field209 + 1 & 31;
        synchronized(this) {
            KeyHandler.hasFocus = volatileFocus;
        }
        this.doCycle();
    }

    static int field4319;

    void graphicsTick() {
        Container var1 = container();
        long var2 = MonotonicClock.currentTimeMillis();
        long var4 = graphicsTickTimes[field4319];
        graphicsTickTimes[field4319] = var2;
        field4319 = field4319 + 1 & 31;
        if (var4 != 0L && var2 > var4) {
            int var6 = (int) (var2 - var4);
            fps = ((var6 >> 1) + 32000) / var6;
        }

        if (++field199 - 1 > 50) {
            field199 -= 50;
            fullRedraw = true;
            canvas.setSize(canvasWidth, canvasHeight);
            canvas.setVisible(true);
            if (var1 == frame) {
                Insets var7 = frame.getInsets();
                canvas.setLocation(var7.left + canvasX, canvasY + var7.top);
            } else {
                canvas.setLocation(canvasX, canvasY);
            }
        }

        if (isCanvasInvalid) {
            replaceCanvas();
        }

        doResize();
        draw(fullRedraw);
        if (fullRedraw) {
            clearBackground();
        }

        fullRedraw = false;
    }

    final void doResize() {
        Bounds bounds = this.getFrameContentBounds();
        if (this.contentWidth != bounds.highX || bounds.highY != this.contentHeight || this.resizeCanvasNextFrame) {
            resizeCanvas();
            resizeCanvasNextFrame = false;
        }

    }

    final void flagResize() {
        resizeCanvasNextFrame = true;
    }

    final synchronized void kill() {
        if (!isKilled) {
            isKilled = true;

            try {
                canvas.removeFocusListener(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                cleanUpForQuit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (frame != null) {
                try {
                    System.exit(0);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }

            if (taskHandler != null) {
                try {
                    taskHandler.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            vmethod1099();
        }
    }

    protected abstract void setUp();

    protected abstract void doCycle();

    public Thread startRunnable(Runnable runnable, int i) {
        Thread thread = new Thread(runnable);
        thread.start();
        thread.setPriority(i);
        return thread;
    }

    protected abstract void draw(boolean var1);

    protected abstract void cleanUpForQuit();

    static Font fontHelvetica;
    static FontMetrics loginScreenFontMetrics;
    static Image image;

    protected final void drawInitial(int var1, String var2, boolean clear) {
        try {
            Graphics graphics = canvas.getGraphics();
            if (fontHelvetica == null) {
                fontHelvetica = new Font("Helvetica", Font.BOLD, 13);
                loginScreenFontMetrics = canvas.getFontMetrics(fontHelvetica);
            }

            if (clear) {
                graphics.setColor(Color.black);
                graphics.fillRect(0, 0, canvasWidth, canvasHeight);
            }

            Color color = new Color(140, 17, 17);

            try {
                if (image == null) {
                    image = canvas.createImage(304, 34);
                }

                Graphics imageGraphics = image.getGraphics();
                imageGraphics.setColor(color);
                imageGraphics.drawRect(0, 0, 303, 33);
                imageGraphics.fillRect(2, 2, var1 * 3, 30);
                imageGraphics.setColor(Color.black);
                imageGraphics.drawRect(1, 1, 301, 31);
                imageGraphics.fillRect(var1 * 3 + 2, 2, 300 - var1 * 3, 30);
                imageGraphics.setFont(fontHelvetica);
                imageGraphics.setColor(Color.white);
                imageGraphics.drawString(var2, (304 - loginScreenFontMetrics.stringWidth(var2)) / 2, 22);
                graphics.drawImage(image, canvasWidth / 2 - 152, canvasHeight / 2 - 18, null);
            } catch (Exception exception) {
                int centerX = canvasWidth / 2 - 152;
                int centerY = canvasHeight / 2 - 18;
                graphics.setColor(color);
                graphics.drawRect(centerX, centerY, 303, 33);
                graphics.fillRect(centerX + 2, centerY + 2, var1 * 3, 30);
                graphics.setColor(Color.black);
                graphics.drawRect(centerX + 1, centerY + 1, 301, 31);
                graphics.fillRect(var1 * 3 + centerX + 2, centerY + 2, 300 - var1 * 3, 30);
                graphics.setFont(fontHelvetica);
                graphics.setColor(Color.white);
                graphics.drawString(var2, centerX + (304 - loginScreenFontMetrics.stringWidth(var2)) / 2, centerY + 22);
            }
        } catch (Exception exception) {
            canvas.repaint();
        }

    }

    public void error(String var1) {
        if (!hasErrored) {
            hasErrored = true;
            System.out.println("error_game_" + var1);

            try {
                getAppletContext().showDocument(new URL(new URL("http://oldschool1.runescape.com/"), "error_game_" + var1 + ".ws"), "_self");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean isActive()
    {
        return true;
    }

    @Override
    public URL getDocumentBase()
    {
        try {
            return new URL("http://oldschool1.runescape.com/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AppletContext getAppletContext()
    {
        return new AppletContext()
        {
            @Override
            public AudioClip getAudioClip(URL url)
            {
                return null;
            }

            @Override
            public Image getImage(URL url)
            {
                return null;
            }

            @Override
            public Applet getApplet(String name)
            {
                return null;
            }

            @Override
            public Enumeration<Applet> getApplets()
            {
                return null;
            }

            @Override
            public void showDocument(URL url)
            {
                if (url.getPath().startsWith("/error_game_"))
                {

                    String code = url.getPath()
                            .replace("/", "")
                            .replace(".ws", "");

                    SwingUtilities.invokeLater(() ->
                            new FatalErrorDialog("Xeros has crashed with the message: " + code + "")
                                    .setTitle("Xeros", "Xeros has crashed")
                                    .addHelpButtons()
                                    .open());
                }
            }

            @Override
            public void showDocument(URL url, String target)
            {
                showDocument(url);
            }

            @Override
            public void showStatus(String status)
            {
            }

            @Override
            public void setStream(String key, InputStream stream) throws IOException
            {
            }

            @Override
            public InputStream getStream(String key)
            {
                return null;
            }

            @Override
            public Iterator<String> getStreamKeys()
            {
                return null;
            }
        };
    }

    Container container() {
        return frame != null ? frame : this;
    }

    protected Bounds getFrameContentBounds() {
        Container container = container();
        int boundsX = Math.max(container.getWidth(), 0);
        int boundsY = Math.max(container.getHeight(), 0);
        if (frame != null) {
            Insets var4 = frame.getInsets();
            boundsX -= var4.left + var4.right;
            boundsY -= var4.top + var4.bottom;
        }

        return new Bounds(boundsX, boundsY);
    }

    protected final boolean hasFrame() {
        return frame != null;
    }

    protected abstract void vmethod1099();

    public final void destroy() {
        if (this == gameEngine && !isKilled) {
            stopTimeMs = MonotonicClock.currentTimeMillis();
            TaskUtils.sleep(5000L);
            kill();
        }
    }

    public final synchronized void paint(Graphics var1) {
        if (this == gameEngine && !isKilled) {
            fullRedraw = true;
            if (MonotonicClock.currentTimeMillis() - field185 > 1000L) {
                Rectangle var2 = var1.getClipBounds();
                if (var2 == null || var2.width >= canvasWidth && var2.height >= canvasHeight) {
                    isCanvasInvalid = true;
                }
            }

        }
    }

    public void onReplaceCanvasNextFrameChanged(int idx) {
        if (Client.instance != null && Client.instance.isGpu() && isReplaceCanvasNextFrame()) {
            setReplaceCanvasNextFrame(false);
            setResizeCanvasNextFrame(true);
        }
    }

    public static int gameCyclesToDo;
    public static Clock clock;

    public static Clock getClock() {
        try {
            return new NanoClock();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return new MilliClock();
        }
    }

    public void run() {
        try {
            if (Signlink.javaVendor != null) {
                String javaVendor = Signlink.javaVendor.toLowerCase();
                if (javaVendor.contains("sun") || javaVendor.contains("apple")) {
                    String javaVersion = Signlink.javaVersion;
                    if (javaVersion.equals("1.1") || javaVersion.startsWith("1.1.") || javaVersion.equals("1.2") || javaVersion.startsWith("1.2.") || javaVersion.equals("1.3") || javaVersion.startsWith("1.3.") || javaVersion.equals("1.4") || javaVersion.startsWith("1.4.") || javaVersion.equals("1.5") || javaVersion.startsWith("1.5.") || javaVersion.equals("1.6.0")) {
                        this.error("wrongjava");
                        return;
                    }

                    if (javaVersion.startsWith("1.6.0_")) {
                        this.error("wrongjava");
                    }

                    fiveOrOne = 5;
                }
            }

            thread = Thread.currentThread();
            thread.setName("Client");

            setFocusCycleRoot(true);
            addCanvas();
            setUp();

            clock = getClock();

            while(0L == stopTimeMs ||  MonotonicClock.currentTimeMillis() < stopTimeMs) {
                gameCyclesToDo = clock.wait(cycleDurationMillis, fiveOrOne);


                for (int cycles = 0; cycles < gameCyclesToDo; ++cycles) {
                    clientTick();
                }

                graphicsTick();
                post(canvas);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            error("crash");
        }

        kill();
    }

    public final void start() {
        if (this == gameEngine && !isKilled) {
            stopTimeMs = 0L;
        }
    }

    public final void focusGained(FocusEvent var1) {
        volatileFocus = true;
        fullRedraw = true;
        final FocusChanged focusChanged = new FocusChanged();
        focusChanged.setFocused(true);
        Client.instance.getCallbacks().post(focusChanged);
    }

    public final void focusLost(FocusEvent var1) {
        volatileFocus = false;
    }

    public final void windowActivated(WindowEvent var1) {
    }

    public final void windowDeactivated(WindowEvent var1) {
    }

    public final void windowDeiconified(WindowEvent var1) {
    }

    public final void windowOpened(WindowEvent var1) {
    }


    public final void stop() {
        if (this == gameEngine && !isKilled) {
            stopTimeMs = MonotonicClock.currentTimeMillis() + 4000L;
        }
    }

    public final void setupMouse() {
        this.canvas.addMouseListener(MouseHandler.instance);
        this.canvas.addMouseMotionListener(MouseHandler.instance);
        this.canvas.addFocusListener(MouseHandler.instance);
    }

    protected MouseWheel mouseWheel() {
        if (this.mouseWheelHandler == null) {
            this.mouseWheelHandler = new MouseWheelHandler();
            this.mouseWheelHandler.addTo(this.canvas);
        }

        return this.mouseWheelHandler;
    }

    protected final void setUpKeyboard() {
        method5264();
        keyHandler.setupComponent(this.canvas);
    }

    static void method5264() {
        if (TaskHandler.javaVendor.toLowerCase().contains("microsoft")) {
            keyCodes[186] = 57;
            keyCodes[187] = 27;
            keyCodes[188] = 71;
            keyCodes[189] = 26;
            keyCodes[190] = 72;
            keyCodes[191] = 73;
            keyCodes[192] = 58;
            keyCodes[219] = 42;
            keyCodes[220] = 74;
            keyCodes[221] = 43;
            keyCodes[222] = 59;
            keyCodes[223] = 28;
        } else {
            keyCodes[44] = 71;
            keyCodes[45] = 26;
            keyCodes[46] = 72;
            keyCodes[47] = 73;
            keyCodes[59] = 57;
            keyCodes[61] = 27;
            keyCodes[91] = 42;
            keyCodes[92] = 74;
            keyCodes[93] = 43;
            keyCodes[192] = 28;
            keyCodes[222] = 58;
            keyCodes[520] = 59;
        }

    }


    public Clipboard clipboard;

    protected void setUpClipboard() {
        this.clipboard = this.getToolkit().getSystemClipboard();
    }

    protected void setClipboardText(String text) {
        this.clipboard.setContents(new StringSelection(text), (ClipboardOwner)null);
    }

    static long field3170;
    static long field4425;

    public final void windowIconified(WindowEvent var1) {
    }

    public final void windowClosed(WindowEvent var1) {
    }

    public final void windowClosing(WindowEvent var1) {
        destroy();
    }

    public final void update(Graphics var1) {
        paint(var1);
    }

    protected void enableDepth(boolean var1) {
        if (this.hasDepth != var1) {
            this.hasDepth = var1;
            Client.rasterProvider.hasDepthPixels(var1);
            Client.rasterProvider.init();
        }
    }

}
