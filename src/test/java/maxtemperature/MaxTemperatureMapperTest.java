package maxtemperature;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.func.sample.hadoop.MaxTemperatureMapper;
import org.func.sample.hadoop.MaxTemperatureReducer;
import org.junit.Test;

public class MaxTemperatureMapperTest {

	@Test
	public void processesValidRecord() throws IOException, InterruptedException {
		System.getProperties().put("hadoop.home.dir", new File("src/main").getAbsolutePath());
		Text value = new Text("0043011990999991950051518004+68750+023550FM-12+0382" +
								"99999V0203201N00261220001CN9999999N9-00111+99999999999");

		new MapDriver<LongWritable, Text, Text, IntWritable>()
			.withMapper(new MaxTemperatureMapper())
			.withInput(new LongWritable(), value)
			.withOutput(new Text("1950"), new IntWritable(-11))
			.runTest();
	}

	@Test
	public void ignoresMissingTemperatureRecord() throws IOException, InterruptedException {
		Text value = new Text("0043011990999991950051518004+68750+023550FM-12+382" +
								"99999V0203201N00261220001CN99999999N9+99991+99999999999");

		new MapDriver<LongWritable, Text, Text, IntWritable>()
			.withMapper(new MaxTemperatureMapper())
			.withInput(new LongWritable(), value)
			.runTest();
	}

	@Test
	public void returnsMaximumIntegerInValues() throws IOException, InterruptedException {
		System.getProperties().put("hadoop.home.dir", new File("src/main").getAbsolutePath());
		new ReduceDriver<Text, IntWritable, Text, IntWritable>()
		.withReducer(new MaxTemperatureReducer())
		.withInput(new Text("1950"), Arrays.asList(new IntWritable(10), new IntWritable(5)))
		.withOutput(new Text("1950"), new IntWritable(10))
		.runTest();

	}
}
