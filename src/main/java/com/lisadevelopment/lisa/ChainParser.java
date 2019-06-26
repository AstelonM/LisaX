package com.lisadevelopment.lisa;

import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.utils.StringUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChainParser {

    private String prefix;
    private ChatListener listener;
    private MessageReceivedEvent event;

    public ChainParser(ChatListener listener, MessageReceivedEvent event) {
        this.prefix = listener.getPrefix();
        this.listener = listener;
        this.event = event;
    }

    public String translate() {
        String text = event.getMessage().getContentRaw();
        return parseCommand(text, prefix.length()).result;
    }

    private CommandIndex parseCommand(String text, int origin){
        StringBuilder result = new StringBuilder();
        boolean finished = false;
        int nextCommandOffset;
        int offset = origin;
        while (!finished) {
            nextCommandOffset = text.indexOf(prefix, offset);
            if (nextCommandOffset == -1) {
                nextCommandOffset = text.length();
                finished = true;
            }
            result.append(text, offset, nextCommandOffset);
            offset = nextCommandOffset;
            if (!finished) {
                String nextCommand = StringUtils.firstWord(text, offset);
                if (nextCommand.length() == prefix.length()) { // If the prefix is without command, just append it
                    result.append(text, offset, offset + prefix.length());
                    offset += prefix.length();
                } else {
                    nextCommand = nextCommand.substring(prefix.length());
                    offset += prefix.length();
                    String nextCommandName = StringUtils.firstWord(nextCommand, listener.getFlagSeparator());
                    if (listener.isNullCommand(nextCommand)) {
                        offset += nextCommand.length() + 1;
                        break;
                    }
                    Command command = listener.findCommand(nextCommandName);
                    if (command == null) { // If the command is invalid, go on to the next one
                        result.append(prefix);
                        continue;
                    }
                    CommandIndex subcommand = resolveSubcommand(text, offset);
                    result.append(subcommand.result);
                    offset = subcommand.endIndex;
                }
            }
            if (offset >= text.length())
                finished = true;
        }
        return new CommandIndex(origin, offset, result.toString());
    }

    private CommandIndex resolveSubcommand(String text, int origin) {
        CommandIndex parsed = parseCommand(text, origin);
        String parsedCommandResult = parsed.result;
        int offset = parsed.endIndex;
        String currentCommandHead = StringUtils.firstWord(parsedCommandResult);
        String currentCommandName = StringUtils.firstWord(currentCommandHead, listener.getFlagSeparator());
        Command currentCommand = listener.findCommand(currentCommandName);
        ExecutionInstance args = new ExecutionInstance(event, currentCommandName, parsedCommandResult);
        args.addParam(listener.getChainedParam());
        if (currentCommand.treatHeader(args))
            currentCommand.treat(args);
        else
            args.setResult(prefix + args.getResult());
        String res = args.getResult();
        if (res == null)
            res = "";
        return new CommandIndex(origin, offset, res);
    }

    private class CommandIndex {
        public final int startIndex;
        public final int endIndex;
        public final String result;

        private CommandIndex(int startIndex, int endIndex, String result) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.result = result;
        }
    }
}
