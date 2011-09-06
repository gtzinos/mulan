package mulan.classifier.lazy;

import junit.framework.Assert;

import org.junit.Test;

public class MLkNNTest extends MultiLabelKNNTest {
	
	private static final int DEFAULT_numOfNeighbors = 10; 
	private static final double DEFAULT_smooth = 1.0;
		
	@Override
	public void setUp(){
	}

	@Test
	public void testTestDefaultParameters(){
		Assert.assertEquals(DEFAULT_numOfNeighbors, learner.numOfNeighbors);
		Assert.assertEquals(DEFAULT_smooth, ((MLkNN)learner).smooth);
		
		// common tests
		Assert.assertTrue(learner.isUpdatable());
	}

}