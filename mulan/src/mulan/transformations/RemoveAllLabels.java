/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mulan.transformations;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
/**
 *
 * @author Stavros
 */
public class RemoveAllLabels {

    public Instances transformInstances(Instances dataSet, int numOfLabels) throws Exception {
        //Indices of attributes to remove
        int indices[] = new int[numOfLabels];
        int k = 0;
        for (int j = 0; j < numOfLabels; j++)
        {
            indices[k] = dataSet.numAttributes() - numOfLabels + j;
            k++;
        }

        Remove remove = new Remove();
        remove.setAttributeIndicesArray(indices);
        remove.setInputFormat(dataSet);
        remove.setInvertSelection(true);
        Instances result = Filter.useFilter(dataSet, remove);
        result.setClassIndex(result.numAttributes() - 1);
        return result;
    }

    public Instance transformInstance(Instance instance, int numLabels) throws Exception
    {
        Instance transformed = new Instance(instance);
        transformed.setDataset(null);
        for (int j = 0; j < numLabels; j++)
            transformed.deleteAttributeAt((transformed.numAttributes() - numLabels + j));
        return transformed;
    }
}
