/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mulan.transformations.multiclass;
import java.util.List;
import weka.core.Instance;

/**
 * Class that implement the Copy-Weight transformation method
 * @author Stavros
 */
public class CopyWeight extends Copy {

    public CopyWeight(int numOfLabels) {
        super(numOfLabels);
    }

    @Override
    List<Instance> transformInstance(Instance instance) {
        List<Instance> copy = super.transformInstance(instance);
        for (Instance anInstance : copy)
            anInstance.setWeight(1.0/copy.size());
        return copy;
    }
}
