package com.lisaxdevelopment.lisax.commands.misc;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;

public class CharCount extends Command {

    public CharCount(ChatListener listener) {
        super(listener);
        name = "charCount";
        description = "Get the number of characters in a text";
        usage = getPrefix() + "charCount (text)";
        aliases = new String[] { "countChars", "countCharacters", "length", "textLength", "getLength", "getTextLength" };
        examples = getPrefix() + "charCount abcdefg";
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
