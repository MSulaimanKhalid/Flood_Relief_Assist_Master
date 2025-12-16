package org.example.zereaab.predictionGraph;

import org.example.zereaab.AssigningDataFromCSV.AssignData;
import org.example.zereaab.AssigningDataFromCSV.WaterNode;

import java.util.ArrayList;
import java.util.List;

public class RiverGraph {

    // References to the real nodes coming from AssignData
    public WaterNode Kabul, TarbelaDam, Kalabagh, Chashma, Taunsa,Marala, Trimmu, Panjnad,
            ManglaDam,Guddu, Sukkur, Kotri;

    public List<WaterNode> waterNodesToShowOnMap = new ArrayList<>();
    public List<RiverEdge> edges = new ArrayList<>();


    public RiverGraph(AssignData data) {
        //reuse the already made nodes
        Kabul = data.Kabul;
        TarbelaDam = data.TarbelaDam;
        Kalabagh = data.Kalabagh;
        Chashma = data.Chashma;
        Taunsa = data.Taunsa;

        Marala = data.Marala;
        Trimmu = data.Trimmu;
        Panjnad = data.Panjnad;
        ManglaDam = data.ManglaDam;

        Guddu = data.Guddu;
        Sukkur = data.Sukkur;
        Kotri = data.Kotri;

        waterNodesToShowOnMap.add(Kabul);
        waterNodesToShowOnMap.add(TarbelaDam);
        waterNodesToShowOnMap.add(Kalabagh);
        waterNodesToShowOnMap.add(Chashma);
        waterNodesToShowOnMap.add(Taunsa);
        waterNodesToShowOnMap.add(Marala);
        waterNodesToShowOnMap.add(Trimmu);
        waterNodesToShowOnMap.add(Panjnad);
        waterNodesToShowOnMap.add(ManglaDam);
        waterNodesToShowOnMap.add(Guddu);
        waterNodesToShowOnMap.add(Sukkur);
        waterNodesToShowOnMap.add(Kotri);


        buildGraphConnections();
    }

    private void buildGraphConnections() {

        edges.add(new RiverEdge(Kabul, TarbelaDam, 2, 0.90));

        edges.add(new RiverEdge(TarbelaDam, Kalabagh, 3, 0.80));
        edges.add(new RiverEdge(Kalabagh, Chashma, 7, 0.85));
        edges.add(new RiverEdge(Chashma, Taunsa, 6, 0.85));
        edges.add(new RiverEdge(Taunsa, Guddu, 2, 0.80));

        edges.add(new RiverEdge(Marala, Trimmu, 3, 0.90));
        edges.add(new RiverEdge(ManglaDam, Trimmu, 1, 0.90));

        edges.add(new RiverEdge(Trimmu, Panjnad, 2, 0.85));
        edges.add(new RiverEdge(Panjnad, Guddu, 1, 0.85));

        edges.add(new RiverEdge(Guddu, Sukkur, 2, 0.80));
        edges.add(new RiverEdge(Sukkur, Kotri, 2, 0.80));
    }


}
