package serviceLoader;

import com.google.auto.service.AutoService;
import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoService(ClusterAnalysisService.class)
public class StandardDeviationCalculator implements ClusterAnalysisService {
    DataSet dataSet = null;

    @Override
    public void setOptions(String[] options) throws ClusteringException {

    }

    @Override
    public String getName() {
        return "Algorytm obliczania odchylenia standardowego";
    }

    @Override
    public void submit(DataSet ds) throws ClusteringException {
        dataSet=ds;
    }

    @Override
    public String retrieve(boolean clear) throws ClusteringException {

        List<Double> values = new ArrayList<>();
        int readColumn=0;

        String headers[] = dataSet.getHeader();
        for (String header : headers)
        {
            if (header.equals("Value"))
            {
                break;
            }
            else
            {
                readColumn++;
            }
        }

        for (int i = 0;i<dataSet.getData().length;i++)
        {
            values.add(Double.valueOf(dataSet.getData()[i][readColumn]));
        }

        String result;

        double sum = 0.0, standardDeviation = 0.0;
        int length = values.size();

        for(double num : values) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: values) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        result = String.valueOf(Math.sqrt(standardDeviation/length));
        return result;
    }
}
