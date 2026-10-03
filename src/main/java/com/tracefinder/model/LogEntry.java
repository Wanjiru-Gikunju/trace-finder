package com.tracefinder.model;

import java.time.LocalDateTime;

 /*  short version using java 21 (Enter: the record)  */

  public record LogEntry (
    LocalDateTime timestamp,
    String level,
    String sourceIp,
    String target,
    String action,
    String originalLine
  ) {
    public LogEntry(LocalDateTime timestamp, String level, String sourceIp, String target, String action) {
      this(timestamp, level, sourceIp, target, action, null);
    }
  }


/* Longer Verssion

public class LogEntry {
    private final LocalDateTime timestamp;
    private final String Level;
    private final String sourceIp;
    private final String target;
    private final String action;

    public LogEntry(LocalDateTime timestamp, String Level, String sourceIp, String target, String action) {
        this.timestamp = timestamp;
        this level = level;
        this.sourceIp = sourceIp;
        this.target = target;
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public String getTarget() {
        return target;
    }

    public String getAction() {
        return action;
    }
    
}

*/
