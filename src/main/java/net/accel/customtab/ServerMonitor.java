package net.accel.customtab;

public class ServerMonitor {

    private int playerCount;
    private int tps;
    private int mspt;

    public void update(int playerCount, int mspt) {
        this.playerCount = playerCount;
        this.mspt = mspt;
        this.tps = (mspt == 0) ? 0 : (1000 / this.mspt);
    }

    public int getTps() {
        return Math.min(tps, 20);
    }

    public int getMspt() {
        return mspt;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String applyArguments(String src) throws IllegalArgumentException {
        var sb = new StringBuilder(src.length());
        int p = 0;
        int prev = 0;
        while (true) {
            if (p == src.length())
                break;
            p = src.indexOf('%', prev);
            if (p == -1)
                break;
            if (p == src.length() - 1)
                break;
            sb.append(src, prev, p);
            ++p;
            var nextChar = src.charAt(p);
            switch (nextChar) {
                case 't' -> sb.append(getTps());
                case 'm' -> sb.append(getMspt());
                case 'c' -> sb.append(getPlayerCount());
                case '%' -> sb.append('%');
                default -> throw new IllegalArgumentException("unknown argument: %" + nextChar);
            }
            prev = ++p;
        }
        if (prev != src.length())
            sb.append(src, prev, src.length());
        return sb.toString();
    }
}
