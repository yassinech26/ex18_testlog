import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StateMachineTest {

    // =========================
    // (D) Impossible — Payment cannot be invoked from BASKET
    // From BASKET only ToCatalog and Finalize are valid
    // =========================
    @Test
    void testSequenceD_Invalid() {
        StateMachine sm = new StateMachine();

        sm.login();         // LOGIN → BROWSE
        sm.addItem();       // BROWSE → BASKET (client sees their basket)

        assertThrows(IllegalStateException.class, () -> {
            sm.payment();   // ❌ BASKET → payment() forbidden
        });
        //assertThrows(ExceptionClass, lambda) — checks that the code inside the lambda throws the specified exception.
        // If no exception is thrown (or a different one is), the test fails.
    }

    // =========================
    // (E) Possible — from CHECKOUT, GoBack moves us to BASKET
    // =========================
    @Test
    void testSequenceE_Valid() {
        StateMachine sm = new StateMachine();

        sm.login();             // LOGIN → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT
        sm.goBack();            // CHECKOUT → BASKET ✅

        assertEquals(StateMachine.State.BASKET, sm.getState());
        //assertEquals(expected, actual) — checks that the current state equals what you expect after a sequence of calls.
        // If they don't match, the test fails.
    }

    // =========================
    // (F) Impossible — from PAY, cannot go back to CHECKOUT
    // The only valid transition from PAY is TransactionConfirmed → LOGOUT
    // =========================
    @Test
    void testSequenceF_Invalid() {
        StateMachine sm = new StateMachine();

        sm.login();             // LOGIN → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT
        sm.payment();           // CHECKOUT → PAY

        assertThrows(IllegalStateException.class, () -> {
            sm.goBack();        // ❌ PAY → goBack() forbidden
        });
    }

    // =========================
    // (G) Valid sequence #1 — full happy path ending in LOGOUT
    // LOGIN → BROWSE → BASKET → CHECKOUT → PAY → LOGOUT
    // =========================
    @Test
    void testSequenceG_Valid() {
        StateMachine sm = new StateMachine();

        sm.login();                     // LOGIN → BROWSE
        sm.addItem();                   // BROWSE → BASKET
        sm.finalizeOrder();             // BASKET → CHECKOUT
        sm.payment();                   // CHECKOUT → PAY
        sm.transactionConfirmed();      // PAY → LOGOUT

        assertEquals(StateMachine.State.LOGOUT, sm.getState());

    }

    // =========================
    // (H) Valid sequence #2 — browse multiple times before checkout
    // LOGIN → BROWSE → BASKET → BROWSE → BASKET → CHECKOUT → PAY
    // =========================
    @Test
    void testSequenceH_Valid() {
        StateMachine sm = new StateMachine();

        sm.login();             // LOGIN → BROWSE
        sm.addItem();           // BROWSE → BASKET
        sm.toCatalog();         // BASKET → BROWSE (browsing again)
        sm.addItem();           // BROWSE → BASKET
        sm.finalizeOrder();     // BASKET → CHECKOUT
        sm.payment();           // CHECKOUT → PAY

        assertEquals(StateMachine.State.PAY, sm.getState());
    }
}