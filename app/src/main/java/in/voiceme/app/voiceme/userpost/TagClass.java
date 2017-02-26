package in.voiceme.app.voiceme.userpost;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Cuneyt on 21.8.2015.
 *
 */
public class TagClass {

    private String id;
    private String name;
    private String color;

    public TagClass() {

    }

    public TagClass(String id, String name) {
        this.id = id;
        this.name = name;
        this.color = getRandomColor();

    }

    public String getRandomColor() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#ED7D31");
        colors.add("#00B0F0");
        colors.add("#FF0000");
        colors.add("#D0CECE");
        colors.add("#00B050");
        colors.add("#9999FF");
        colors.add("#FF5FC6");
        colors.add("#FFC000");
        colors.add("#7F7F7F");
        colors.add("#4800FF");

        return colors.get(new Random().nextInt(colors.size()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String code) {
        this.id = code;
    }


}
