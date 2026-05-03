import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StateMachineTest {

    // =========================
    // (A) Valid sequence
    // Starts mid-flow in BROWSE (already logged in)
    // BROWSE → BASKET → CHECKOUT → BASKET → CHECKOUT
    // =========================
    @Test
    void testSequenceA() {
        StateMachine sm = new StateMachine();

        // We need to reach BROWSE first (LOGIN → BROWSE)
        sm.login();             // LOGIN → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT
        sm.goBack();            // CHECKOUT → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT

        assertEquals(StateMachine.State.CHECKOUT, sm.getState());
    }

    // =========================
    // (B) Valid sequence — full purchase path
    // LOGIN → BROWSE → BASKET → CHECKOUT → PAY
    // =========================
    @Test
    void testSequenceB() {
        StateMachine sm = new StateMachine();

        sm.login();             // LOGIN → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT
        sm.payment();           // CHECKOUT → PAY

        assertEquals(StateMachine.State.PAY, sm.getState());
    }

    // =========================
    // (B) Alternative start — same destination
    // BASKET → BROWSE → BASKET → CHECKOUT → PAY
    // =========================
    @Test
    void testSequenceB_Alternative() {
        StateMachine sm = new StateMachine();

        sm.login();             // LOGIN → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.toCatalog();         // BASKET → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT
        sm.payment();           // CHECKOUT → PAY

        assertEquals(StateMachine.State.PAY, sm.getState());
    }

    // =========================
    // (C) Valid sequence — starts from CHECKOUT
    // CHECKOUT → BASKET → BROWSE
    // =========================
    @Test
    void testSequenceC() {
        StateMachine sm = new StateMachine();

        // Reach CHECKOUT first
        sm.login();             // LOGIN → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT

        // Now the sequence being tested
        sm.goBack();            // CHECKOUT → BASKET
        sm.toCatalog();         // BASKET → BROWSE

        assertEquals(StateMachine.State.BROWSE, sm.getState());
    }

    // =========================
    // (D) Invalid — ToCatalog cannot be called twice in a row
    // From BROWSE, ToCatalog is illegal (only allowed from BASKET)
    // =========================
    @Test
    void testSequenceD_Invalid() {
        StateMachine sm = new StateMachine();

        sm.login();             // LOGIN → BROWSE

        // Already in BROWSE, calling toCatalog() is illegal
        assertThrows(IllegalStateException.class, () -> {
            sm.toCatalog();     // ❌ BROWSE → toCatalog() forbidden
        });
    }

    // =========================
    // (E) Invalid — Payment cannot be called from BASKET
    // After AddItem we are in BASKET, Payment requires CHECKOUT
    // =========================
    @Test
    void testSequenceE_Invalid() {
        StateMachine sm = new StateMachine();

        sm.login();             // LOGIN → BROWSE
        sm.addItem();           // BROWSE → BASKET

        assertThrows(IllegalStateException.class, () -> {
            sm.payment();       // ❌ BASKET → payment() forbidden
        });
    }

    // =========================
    // (F) Valid long sequence
    // LOGIN → BROWSE → BASKET → BROWSE → BASKET → CHECKOUT → BASKET → CHECKOUT → PAY
    // =========================
    @Test
    void testSequenceF() {
        StateMachine sm = new StateMachine();

        sm.login();             // LOGIN → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.toCatalog();         // BASKET → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT
        sm.goBack();            // CHECKOUT → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT
        sm.payment();           // CHECKOUT → PAY

        assertEquals(StateMachine.State.PAY, sm.getState());
    }
}