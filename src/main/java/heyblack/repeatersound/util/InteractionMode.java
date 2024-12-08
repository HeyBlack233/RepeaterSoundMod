package heyblack.repeatersound.util;

public enum InteractionMode
{
    NORMAL("normal"),
    ALARM("alarm"),
    DISABLED("disabled");

    public final String id;

    InteractionMode(String id) {
        this.id = id;
    }
}
