package utils;

public class JsonBuilder {

    public static String escape(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }

    public static String stringListToJson(java.util.List<String> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(escape(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
