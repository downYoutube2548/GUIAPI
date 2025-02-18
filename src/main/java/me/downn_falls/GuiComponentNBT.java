package me.downn_falls;

import me.downn_falls.component.GuiComponent;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Base64;

public class GuiComponentNBT {

    public static String serialize(@NotNull GuiComponent component) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(component);
                objectOutputStream.flush();
            }
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GuiComponent deserialize(String s) {
        try {
            return (GuiComponent) deserializeFromString(s);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object deserializeFromString(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(string);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return objectInputStream.readObject();
        }
    }
}
