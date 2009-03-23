
package mulan.classifier.neural;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Implementation of basic neural network. The network consists of one input layer, 
 * zero or more hidden layers and one output layer. Each layer contains 1 or more 
 * {@link Neuron} units. The input layer is used just to store and forward input 
 * pattern of the network to first hidden layer for processing. 
 * Input layer do not process input pattern. Neurons of input layer have one input weight 
 * equal to 1, bias weight equal to 0 and use linear activation function.
 * 
 * @author Jozef Vilcek
 */
public class BasicNeuralNet implements NeuralNet, Serializable {

	private static final long serialVersionUID = -8944873770650464701L;

	private final List<List<Neuron>> layers;
	private double[] currentNetOutput;
	private final int netInputDim;
	private final int netOutputDim;
	
	
	/**
	 * Creates a {@link BasicNeuralNet} instance.
	 * 
	 * @param netTopology defines a topology of the network. The array length corresponds 
	 * 		to number of network layers. The values of the array corresponds to number 
	 * 		of neurons in each particular layer. 
	 * @param biasInput the bias input value for neurons of the neural network.
	 * @param activationFunction the type of activation function to be used by network elements
	 * @throws IllegalArgumentException if network topology is incorrect of activation function class is null.
	 */
	public BasicNeuralNet(int[] netTopology, double biasInput, 
			Class<? extends ActivationFunction> activationFunction){
		
		if(netTopology == null || netTopology.length < 2){
			throw new IllegalArgumentException("The topology for neural network is not specified " +
					"or is invalid. Please provide correct topology for the network.");
		}
		if(activationFunction == null){
			throw new IllegalArgumentException("The activation functions is null.");
		}
		
		netInputDim = netTopology[0];
		netOutputDim = netTopology[netTopology.length - 1];
		layers = new ArrayList<List<Neuron>>(netTopology.length);
		// set up input layer
		List<Neuron> inputLayer = new ArrayList<Neuron>(netTopology[0]);
		for(int n = 0; n < netTopology[0]; n++){
			Neuron neuron = new Neuron(new ActivationLinear(), 1, biasInput);
			double[] weights = neuron.getWeights();
			weights[0] = 1;
			weights[1] = 0;
			inputLayer.add(neuron);
		}
		layers.add(inputLayer);
		
		// set up other layers
		try {
			for(int index = 1; index<netTopology.length; index++){
				// create neurons of a layer
				List<Neuron> layer = new ArrayList<Neuron>(netTopology[index]);
				for(int n = 0; n < netTopology[index]; n++){
					Neuron neuron = new Neuron(activationFunction.newInstance(), 
												netTopology[index - 1], biasInput);
					layer.add(neuron);
				}
				layers.add(layer);
				// add forward connections between layers
				List<Neuron> prevLayer = layers.get(index - 1);
				for(int n = 0; n < prevLayer.size(); n++){
					prevLayer.get(n).addAllNeurons(layer);
				}
			}
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Failed to create activation function instance.", e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Failed to create activation function instance.", e);
		}
	}
	
	public List<Neuron> getLayerUnits(int layerIndex) {

		return  Collections.unmodifiableList(layers.get(layerIndex));
	}

	public int getLayersCount() {
		return layers.size();
	}

	public double[] feedForward(final double[] inputPattern) {
		
		if(inputPattern == null || inputPattern.length != netInputDim){
			throw new IllegalArgumentException("Specified input pattern vector is null " +
					"or does not match network input dimension.");
		}
		
		double[] layerOutput = null;
		double[] layerInput = inputPattern;
		for(int layerIndex = 0; layerIndex<layers.size(); layerIndex++){
			List<Neuron> layer = layers.get(layerIndex);
			int layerSize = layer.size();
			layerOutput = new double[layerSize];
			for(int n = 0; n < layerSize; n++){
				if(layerIndex == 0)
					layerOutput[n] = layer.get(n).processInput(new double[]{layerInput[n]});
				else
					layerOutput[n] = layer.get(n).processInput(layerInput);
			}
			layerInput = Arrays.copyOf(layerOutput, layerOutput.length); 
		}
		
		currentNetOutput = Arrays.copyOf(layerOutput, layerOutput.length);
		return currentNetOutput;
	}
	
	public double[] getOutput() {
		if(currentNetOutput == null){
			return new double[netOutputDim];
		}

		return currentNetOutput;
	}

	public void reset() {
		currentNetOutput = null;
		for(List<Neuron> layer : layers)
			for(Neuron neuron : layer)
				neuron.reset();
	}

	public int getNetInputSize() {
		return netInputDim;
	}

	public int getNetOutputSize() {
		return netOutputDim;
	}
}