package kr.spring.batch.chapter06.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.xml.StaxWriterCallback;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * kr.spring.batch.chapter06.file.ProductHeaderStaxCallback
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오전 10:19
 */
@Slf4j
public class ProductHeaderStaxCallback implements StaxWriterCallback {

	@Override
	public void write(XMLEventWriter writer) throws IOException {
		try {
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();

			XMLEvent event = eventFactory.createStartElement("", "", "header");
			writer.add(event);
			event = eventFactory.createAttribute("generated", DateFormat.getDateTimeInstance().format(new Date()));
			writer.add(event);
			event = eventFactory.createEndElement("", "", "header");
			writer.add(event);
		} catch (XMLStreamException ignored) {
			log.warn("Header를 쓰는데 예외가 발생했습니다.", ignored);
		}
	}
}
