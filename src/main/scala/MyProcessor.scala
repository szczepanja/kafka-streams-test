import org.apache.kafka.streams.processor.api.{Processor, Record}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.serialization.Serdes
import org.apache.kafka.streams.state.Stores

import java.util.Locale

class MyProcessor extends Processor[String, String, String, String] {

  val store = Stores.keyValueStoreBuilder(
    Stores.persistentKeyValueStore("invStore"),
    Serdes.stringSerde, Serdes.stringSerde).build()

  override def process(record: Record[String, String]): Unit = {
    val words = record
      .value()
      .toLowerCase(Locale.getDefault())
      .split("\\W+")

    for (word <- words) store.get(word)
  }

}

object MyProcessor extends App {

  val INPUT_TOPIC = "input-topic"
  val OUTPUT_TOPIC = "output-topic"

  val builder = new StreamsBuilder

  import org.apache.kafka.streams.scala.ImplicitConversions._
  import org.apache.kafka.streams.scala.serialization.Serdes._
//
//  builder.stream[String, String](INPUT_TOPIC)
//    .process(() => new MyProcessor(), "store")
//    .print(System.out)

}