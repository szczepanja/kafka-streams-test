import org.apache.kafka.streams.{KeyValue, TestInputTopic, TestOutputTopic, Topology, TopologyTestDriver}
import org.apache.kafka.streams.state.ValueAndTimestamp
import org.apache.kafka.streams.state.internals.MeteredKeyValueStore
import org.scalatest.flatspec._
import org.scalatest.matchers.should
import scala.jdk.CollectionConverters.MapHasAsScala

class KafkaDemoSpec extends AnyFlatSpec with should.Matchers {

  import org.apache.kafka.streams.scala.serialization.Serdes._

  def helper() = new {
    val topology: Topology = KafkaDemo.getTopology
    val testDriver = new TopologyTestDriver(topology)
    val inputTopic: TestInputTopic[String, String] = testDriver.createInputTopic(KafkaDemo.INPUT_TOPIC, stringSerde.serializer, stringSerde.serializer())
    val outputTopic: TestOutputTopic[String, String] = testDriver.createOutputTopic(KafkaDemo.OUTPUT_TOPIC, stringSerde.deserializer, stringSerde.deserializer)
    val stateStore: MeteredKeyValueStore[String, ValueAndTimestamp[Long]] = testDriver
      .getAllStateStores
      .asScala
      .head
      ._2
      .asInstanceOf[MeteredKeyValueStore[String, ValueAndTimestamp[Long]]]
  }

  it should "return topology" in {
    val topology = helper()

    topology.inputTopic.pipeInput("value value ania ma kota i psa")
    topology.outputTopic.readKeyValue() shouldBe KeyValue.pair("value", "1")
  }

  it should "return aggregated number of value" in {
    val topology = helper()

    topology.inputTopic.pipeInput("value value ania ma kota i psa")
    topology.stateStore.get("value").value() shouldBe 2
  }

  it should "return size of aggregated values in store" in {
    val topology = helper()

    topology.inputTopic.pipeInput("value value ania ma kota i psa")
    println(topology.stateStore.all())
  }

}
