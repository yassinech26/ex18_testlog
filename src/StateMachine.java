public class StateMachine {


    //The StateMachine class enforces which steps are allowed and in which order.
    enum State {
        LOGIN, BROWSE, BASKET, CHECKOUT, PAY, LOGOUT
    }

    private State currentState = State.LOGIN;

    public State getState() {
        return currentState;
    }

    public void login() {
        if (currentState == State.LOGIN) {
            currentState = State.BROWSE; // ✅ Fixed: LOGIN → BROWSE
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    public void toCatalog() {
        if (currentState == State.BASKET) {
            currentState = State.BROWSE; // ✅ correct
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    public void addItem() {
        if (currentState == State.BROWSE) {
            currentState = State.BASKET; // ✅ correct
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    public void finalizeOrder() {
        if (currentState == State.BASKET) {
            currentState = State.CHECKOUT; // ✅ correct
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    public void goBack() {
        if (currentState == State.CHECKOUT) {
            currentState = State.BASKET; // ✅ correct
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    public void payment() {
        if (currentState == State.CHECKOUT) {
            currentState = State.PAY; // ✅ correct
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    // ✅ Fixed: new method, was completely missing
    public void transactionConfirmed() {
        if (currentState == State.PAY) {
            currentState = State.LOGOUT;
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }
}