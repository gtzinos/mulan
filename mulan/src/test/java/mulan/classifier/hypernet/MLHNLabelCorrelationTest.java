package mulan.classifier.hypernet;

import mulan.classifier.MultiLabelLearner;
import mulan.classifier.MultiLabelLearnerBase;
import mulan.classifier.MultiLabelLearnerTestBase;
import mulan.classifier.transformation.BinaryRelevance;
import org.junit.Before;
import weka.classifiers.trees.J48;

public class MLHNLabelCorrelationTest extends MultiLabelLearnerTestBase {

    protected MLHNLabelCorrelation learner;

    @Override
    protected MultiLabelLearnerBase getLearner() {
        return learner;
    }

    @Before
    public void setUp() {
        MultiLabelLearner baseLeaner = new BinaryRelevance(new J48());
        MultiLabelHyperNetWorkType type = MultiLabelHyperNetWorkType.MLHN_GC;
        learner = new MLHNLabelCorrelation(baseLeaner, type);
    }

}
