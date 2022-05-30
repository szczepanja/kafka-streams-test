import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsConfig, Topology}
import org.apache.kafka.streams.kstream.{Named, Printed, Transformer, TransformerSupplier}
import org.apache.kafka.streams.processor.api.{Processor, ProcessorSupplier, Record}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.scala.serialization.Serdes
import org.apache.kafka.streams.state.Stores

import java.util.{Locale, Properties}

class MyProcessor extends Processor[String, String, Void, Void] {

  val store = Stores.keyValueStoreBuilder(
    Stores.persistentKeyValueStore("invStore"),
    Serdes.stringSerde, Serdes.stringSerde).build()

  override def process(record: Record[String, String]): Unit = {

    val words = record
      .value()
      .toLowerCase(Locale.getDefault())
      .split("\\W+")

    for (word <- words) println(word)

  }
}

object MyProcessor extends App {

  val INPUT_TOPIC = "input-topic"
  val OUTPUT_TOPIC = "output-topic"

  import org.apache.kafka.streams.scala.ImplicitConversions._
  import org.apache.kafka.streams.scala.serialization.Serdes._

  val processor = new ProcessorSupplier[String, String, Void, Void] {
    override def get(): Processor[String, String, Void, Void] = new MyProcessor()
  }

  val builder = new StreamsBuilder

  builder
    .stream[String, String]("ania-input")
    .process(processor, Named.as("WordProcessor"))
  val topology: Topology = builder.build()
  println(topology.describe())

  import org.apache.kafka.common.serialization.Serdes

  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-dockerized-app")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ":9092")
  props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
  props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass)

  val ks = new KafkaStreams(topology, props)
}