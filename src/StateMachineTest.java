import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StateMachineTest {

    // =========================
    // (A) Séquence valide
    // =========================
    @Test
    void testSequenceA() {
        StateMachine sm = new StateMachine();

        sm.login();
        sm.toCatalog();
        sm.addItem();
        sm.finalizeOrder();
        sm.goBack();
        sm.finalizeOrder();

        assertEquals(StateMachine.State.CHECKOUT, sm.getState());
    }

    // =========================
    // (B) Séquence valide
    // =========================
    @Test
    void testSequenceB() {
        StateMachine sm = new StateMachine();

        sm.login();
        sm.toCatalog();
        sm.addItem();
        sm.finalizeOrder();
        sm.payment();

        assertEquals(StateMachine.State.PAY, sm.getState());
    }

    // =========================
    // (C) Séquence valide
    // =========================
    @Test
    void testSequenceC() {
        StateMachine sm = new StateMachine();

        sm.login();
        sm.finalizeOrder(); // supposé déjà en BASKET après login
        sm.goBack();
        sm.toCatalog();

        assertEquals(StateMachine.State.BROWSE, sm.getState());
    }

    // =========================
    // (D) Séquence invalide
    // =========================
    @Test
    void testSequenceD_Invalid() {
        StateMachine sm = new StateMachine();

        sm.login();
        sm.toCatalog();

        assertThrows(IllegalStateException.class, () -> {
            sm.toCatalog(); // interdit deux fois
        });
    }

    // =========================
    // (E) Séquence invalide
    // =========================
    @Test
    void testSequenceE_Invalid() {
        StateMachine sm = new StateMachine();

        sm.login();
        sm.toCatalog();
        sm.addItem();

        assertThrows(IllegalStateException.class, () -> {
            sm.payment(); // interdit depuis BASKET
        });
    }

    // =========================
    // (F) Séquence valide longue
    // =========================
    @Test
    void testSequenceF() {
        StateMachine sm = new StateMachine();

        sm.login();
        sm.toCatalog();
        sm.addItem();
        sm.toCatalog();
        sm.addItem();
        sm.finalizeOrder();
        sm.goBack();
        sm.finalizeOrder();
        sm.payment();

        assertEquals(StateMachine.State.PAY, sm.getState());
    }
}