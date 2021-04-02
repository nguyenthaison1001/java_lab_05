package utility;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;

/**
 * Adapter class supports to unmarshal XML.
 */
public class ZonedDateTimeXMLAdapter extends XmlAdapter<String, ZonedDateTime> {
    @Override
    public ZonedDateTime unmarshal(String v) {
        return ZonedDateTime.now();
    }

    @Override
    public String marshal(ZonedDateTime v) {
        return v.toString();
    }
}
