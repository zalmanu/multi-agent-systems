package ro.uvt.mas;

public class Message {
    private Topic topic;
    private int content;

    public Message(Topic topic, int content) {
        this.topic = topic;
        this.content = content;
    }

    public Message() {}

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public Topic getTopic() {
        return topic;
    }

    public int getContent() {
        return content;
    }


}
