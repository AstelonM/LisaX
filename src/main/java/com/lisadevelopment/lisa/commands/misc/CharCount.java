package com.lisadevelopment.lisa.commands.misc;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;

public class CharCount extends Command {

    public CharCount(ChatListener listener) {
        super(listener);
        name = "charCount";
        description = "Get the number of characters in a text";
        usage = getPrefix() + "charCount (text)";
        aliases = new String[] { "countChars", "countCharacters", "length", "textLength", "getLength", "getTextLength" };
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        String text = instance.getText();
        if (!text.contains(" ")) {
            sendMessage(instance, "0");
            instance.setResult("0");
        } else {
            text = text.substring(text.indexOf(" ")).trim();
            sendMessage(instance, "" + text.length());
            instance.setResult("" + text.length());
        }
    }
}
