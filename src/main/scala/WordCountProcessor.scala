import org.apache.kafka.streams.processor.api.{Processor, Record}

class WordCountProcessor extends Processor[String, String, String, String] {

  override def process(record: Record[String, String]): Unit = ???

}
