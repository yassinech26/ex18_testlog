public class StateMachine {

    enum State {
        LOGIN, BROWSE, BASKET, CHECKOUT, PAY, LOGOUT
    }

    private State currentState = State.LOGIN;

    public State getState() {
        return currentState;
    }

    public void login() {
        if (currentState == State.LOGIN) {
            currentState = State.BASKET;
        }
    }

    public void toCatalog() {
        if (currentState == State.BASKET) {
            currentState = State.BROWSE;
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    public void addItem() {
        if (currentState == State.BROWSE) {
            currentState = State.BASKET;
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    public void finalizeOrder() {
        if (currentState == State.BASKET) {
            currentState = State.CHECKOUT;
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    public void goBack() {
        if (currentState == State.CHECKOUT) {
            currentState = State.BASKET;
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }

    public void payment() {
        if (currentState == State.CHECKOUT) {
            currentState = State.PAY;
        } else {
            throw new IllegalStateException("Invalid transition");
        }
    }
}