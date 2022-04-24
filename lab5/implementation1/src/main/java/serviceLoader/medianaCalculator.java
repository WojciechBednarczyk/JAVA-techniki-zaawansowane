package serviceLoader;

import com.google.auto.service.AutoService;
import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoService(ClusterAnalysisService.class)
public class medianaCalculator implements ClusterAnalysisService {

    DataSet dataSet = null;

    @Override
    public void setOptions(String[] options) throws ClusteringException {

    }

    @Override
    public String getName() {
        return "Algorytm obliczania mediany";
    }

    @Override
    public void submit(DataSet ds) throws ClusteringException {
        dataSet=ds;
    }

    @Override
    public String retrieve(boolean clear) throws ClusteringException {

        List<Integer> values = new ArrayList<>();
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
            values.add(Integer.valueOf(dataSet.getData()[i][readColumn]));
        }

        Collections.sort(values);

        String result;

        if (values.size()%2!=0)
        {
            result = String.valueOf(values.get(values.size()/2));
        }
        else
        {
            double value1 = values.get(values.size()/2);
            double value2 = values.get((values.size()/2)-1);
            result = String.valueOf((value1+value2)/2);
        }
        return result;
    }
}
