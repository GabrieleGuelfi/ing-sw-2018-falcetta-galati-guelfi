package it.polimi.se2018.message;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.view.VisitorView;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestMessageForView {

    Die die = new Die(Colour.WHITE);
    WindowPattern windowPattern = new WindowPattern("foo", 3);
    VisitorView visitorView = new VisitorView() {
        @Override
        public void visit(Message message) {

        }

        @Override
        public void visit(MessageError message) {

        }

        @Override
        public void visit(MessageNickname message) {

        }

        @Override
        public void visit(MessagePrivObj message) {

        }

        @Override
        public void visit(MessagePublicObj message) {

        }

        @Override
        public void visit(MessageTool message) {

        }

        @Override
        public void visit(MessageChooseWP message) {

        }

        @Override
        public void visit(MessageWPChanged message) {

        }

        @Override
        public void visit(MessageTurnChanged message) {

        }

        @Override
        public void visit(MessageDPChanged message) {

        }

        @Override
        public void visit(MessageConfirmMove message) {

        }

        @Override
        public void visit(MessageErrorMove message) {

        }

        @Override
        public void visit(MessagePing message) {

        }

        @Override
        public void visit(MessageRoundChanged message) {

        }

        @Override
        public void visit(MessageRoundTrack message) {

        }

        @Override
        public void visit(MessageEndMatch message) {

        }

        @Override
        public void visit(MessageToolOrder message) {

        }

        @Override
        public void visit(MessageAskMove message) {

        }

        @Override
        public void visit(MessageForceMove message) {

        }

        @Override
        public void visit(MessageCustomWP message) {

        }
    };

    @Test
    public void testMessageForceMove() {

        MessageForceMove messageForceMove = new MessageForceMove("foo", die, windowPattern, false, false, false);
        assertEquals(die, messageForceMove.getDie());
        assertEquals(windowPattern, messageForceMove.getWindowPattern());
        assertEquals(false, messageForceMove.isCanChoose());
        assertEquals(false, messageForceMove.isNewValue());
        assertEquals(false, messageForceMove.isPlacedDie());

        messageForceMove.accept(visitorView);
    }

    @Test
    public void testMessageChooseWP() {
        MessageChooseWP messageChooseWP = new MessageChooseWP("foo", 1, 2, "bar");
        assertEquals(1, messageChooseWP.getFirstIndex());
        assertEquals(2, messageChooseWP.getSecondIndex());
        assertEquals("bar", messageChooseWP.getFile());

        messageChooseWP.accept(visitorView);
    }

    @Test
    public void testMessageNickname() {
        MessageNickname messageNickname = new MessageNickname(true);
        assertEquals(true, messageNickname.getBoolean());

        messageNickname.accept(visitorView);

    }

    @Test
    public void testMessageTimeFinished() {
        MessageTimeFinished messageTimeFinished = new MessageTimeFinished("foo");
        assertEquals(true, messageTimeFinished.isTimeFinished());

        messageTimeFinished.accept(visitorView);
    }

    @Test
    public void testMessageToolOrder() {
        MessageToolOrder m = new MessageToolOrder("foo", 1, 1, true);
        MessageToolOrder m1 = new MessageToolOrder("foo", 1, true);
        MessageToolOrder m2 = new MessageToolOrder("foo", 1, 1, 1, 1);

        assertEquals("foo", m.getNickname());
        assertEquals("foo", m1.getNickname());
        assertEquals("foo", m2.getNickname());

        assertEquals(1, m.getDiceFromWp());
        assertEquals(1, m1.getDiceFromDp());
        assertEquals(1, m2.getDiceFromDp());

        assertEquals(1, m.getPositionInWp());
        assertEquals(true, m1.isAskPlusOrMinusOne());
        assertEquals(1, m2.getPositionInWp());

        assertEquals(true, m.isCanReduceDiceFromWP());
        assertEquals(1, m2.getDiceFromRoundtrack());

        m.accept(visitorView);
        m1.accept(visitorView);
        m2.accept(visitorView);
    }
}
