package charlie.bililivelib;

import charlie.bililivelib.danmaku.DanmakuReceiver;
import charlie.bililivelib.danmaku.dispatch.*;
import charlie.bililivelib.danmaku.event.DanmakuAdapter;
import charlie.bililivelib.danmaku.event.DanmakuEvent;
import charlie.bililivelib.util.I18n;
import charlie.bililivelib.room.Room;
import charlie.bililivelib.session.Session;
import charlie.bililivelib.smalltv.SmallTVProtocol;
import charlie.bililivelib.util.LogUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class DanmakuReceiverAndSmallTVTest {
    private static Session session;

    @BeforeClass
    public static void init() {
        I18n.init();
        LogUtil.init();
        session = new Session(Globals.get().getConnectionPool());
    }

    public static final int SIXTEEN_ROOMID = 10313;
    public static final int CHARLIEJIANG_ROOMID = 39249;
    public static final int MARCOV_ROOMID = 17029;

    @Test
    public void test() throws Exception {
        Room room = new Room(SIXTEEN_ROOMID, session);
        DanmakuReceiver receiver = new DanmakuReceiver(room);
        receiver.getDispatchManager().registerDispatcher(new DanmakuDispatcher());
        receiver.getDispatchManager().registerDispatcher(new StartStopDispatcher());
        receiver.getDispatchManager().registerDispatcher(new WelcomeVipDispatcher());
        receiver.getDispatchManager().registerDispatcher(new GiveGiftDispatcher());
        receiver.getDispatchManager().registerDispatcher(new GlobalGiftDispatcher());
        receiver.getDispatchManager().registerDispatcher(new GlobalAnnounceDispatcher());
        receiver.addDanmakuListener(new TestListener());
        receiver.connect();
        synchronized (this) {
            wait();
        }
    }

    private class TestListener extends DanmakuAdapter {
        private Logger logger = LogManager.getLogger(BiliLiveLib.class);
        private SmallTVProtocol smallTVProtocol = new SmallTVProtocol(session);

        @Override
        public void welcomeVipEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "Welcome VIP:" + event.getParam());
        }

        @Override
        public void danmakuEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "New Danmaku:" + event.getParam());
        }

        @Override
        public void watcherCountEvent(DanmakuEvent event) {
            //logger.log(Level.INFO, "WatcherCount:" + event.getParam());
        }

        @Override
        public void startStopEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "StartStop:" + event.getParam());
        }

        @Override
        public void errorEvent(DanmakuEvent event) {
            logger.log(Level.ERROR, event.getParam());
        }

        @Override
        public void statusEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "STATUS:" + event.getParam());
        }

        @Override
        public void giveGiftEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "Give gift:" + event.getParam());
        }

        @Override
        public void globalGiftEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "Global SmallTVRoom:" + event.getParam());
            //smallTVProtocol.getCurrentSmallTV(((SmallTVRoom) event.getParam()).getRealRoomID());
        }

        @Override
        public void globalAnnounceEvent(DanmakuEvent event) {
            logger.log(Level.INFO, "GlobalAnnounce:" + event.getParam());
        }
    }
}