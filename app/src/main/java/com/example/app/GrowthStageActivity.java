package com.example.app;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GrowthStageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_stage);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        List<Stage> stages = new ArrayList<>();
        stages.add(new Stage("Pag-usbong",
                "Kini mao ang sugod diin ang binhi sa humay nag-absorb og tubig ug nagsugod og pagtubo. Una, magsugod ang gamut (radikulo), ug sunod niini magsugod ang sanga.",
                "•Seed Selection: Gamita ang de-kalidad, walay sakit nga mga binhi nga adunay taas nga rate sa pagtubo.\n" +
                        "•Seed Treatment: Trataron ang mga binhi gamit ang fungicide o biofertilizer aron mapanalipdan batok sa mga pathogen.\n" +
                        "•Water Management: Siguroha ang igo apan dili sobra nga kaumog alang sa optimal nga pagtubo sa binhi.", R.drawable.germination));

        stages.add(new Stage("Paglaki sa Binhi",
                "Niining yugto, ang halaman sa palay nagapamunga og dugang mga sanga gikan sa base sa pangunahing tangkay. Tawag niini nga “tillers” ang mga sanga, ug kini mahimong produktibong mga sanga. Ang bilang sa tillers mahimong maglainon base sa mga faktor sama sa klase sa palay, pagkabutang sa tanom, ug kahandaan sa sustansya.",
                "•Nursery Management: Gamita ang maayong pagkaandam nga higdaanan sa nursery nga adunay husto nga drainage ug pagkamabungahon.\n" +
                        "•Transplanting Timing:  Ibalhin ang mga semilya sa husto nga edad (kasagaran 20-30 ka adlaw ang edad) aron maminusan ang shock sa pagbalhin.\n" +
                        "•Spacing: Tumanon ang angay nga gibug-aton sa tanom aron malikayan ang sobra nga pagdagsang ug masiguro ang maayong sirkulasyon sa hangin.", R.drawable.seedling));

        stages.add(new Stage("Pagtubo sa Mga Sanga",
                "In this stage, the rice plant begins to produce additional shoots from the base of the main stem. These shoots are called tillers and eventually develop into productive stems. The number of tillers can vary depending on factors such as variety, plant spacing, and nutrient availability.",
                "•Nutrient Management: I-apply ang nitrogen fertilizers (urea) sa split doses aron mahikay ang pagtubo sa sanga.\n" +
                        "•Management: Padayonon ang mabaw nga pagbaha aron mapalambo ang himsog nga paglambo sa sanga.\n" +
                        "•Weed Control: Ipahigayon ang epektibo nga pagdumala sa sagbot aron maminusan ang kompetisyon alang sa mga nutrisyon.", R.drawable.tillering));

        stages.add(new Stage("Pag-elongasyon sa Tangkay",
                " Ang pangunahing tangkay ug mga sanga nagahinay og pagdako paingon sa itaas. Ang halaman sa palay nagakuha og karakteristikang matarong nga hitsura samtang nagpangita sa kahayag sa adlaw.",
                "•Nutrient Boost: I-apply ang balanse nga dosis sa mga fertilizers, lakip na ang nitrogen, phosphorus, ug potassium.\n" +
                        "•Pest Management: Bantayan ug kontrolon ang mga peste sama sa mga stem borers ug leafhoppers nga makadaot sa nagtubo nga tanom.\n" +
                        "•Water Management: Sigurohon ang padayon nga suplay sa tubig nga walay pagbaha.", R.drawable.stem_elongnation));

        stages.add(new Stage("Simula sa Panikulo",
                "Ang enerhiya nagasentro na sa reproductive growth. Nagahimo na ang mga panikulo, nga naglakip sa mga bulaklak sa palay, sa mga punta sa pangunahing tangkay ug mga sanga.",
                "•Nutrient Application: I-apply ang katapusang dosis sa nitrogen fertilizer aron suportahan ang pagpalambo sa pamiyuos.\n" +
                        "•Disease Control: Gamiton ang mga fungicide o mga barayti nga makasukol sa mga sakit sama sa rice blast ug sheath blight.\n" +
                        "•Water Management: Padayonon ang optimal nga lebel sa tubig aron suportahan ang pagtubo sa reproduktibo.", R.drawable.panicle_initiation));

        stages.add(new Stage("Yugto sa pag-laki",
                "Ang mga panikulo kompleto nang magsugod og mubunga ang mga bulaklak sa palay. Matag bulaklak adunay stamen (lalaki nga bahin sa reproductive organ) ug pistil (babaye nga bahin sa reproductive organ).",
                "•Micro-nutrient Supply: Sigurohon ang pagkaanaa sa hinungdanon nga mga micronutrients sama sa zinc ug boron aron suportahan ang pagpamulak.\n" +
                        "•Pest Control: Panalipdan batok sa mga peste sama sa panicle mites ug mga insekto nga mokaon sa lugas.\n" +
                        "•Water Management: Padayonon ang pagbaha sa uma apan likayan ang lawom nga tubig nga makalumos sa mga pamiyuos.", R.drawable.heading));

        stages.add(new Stage("Paglalabas sa Bulaklak",
                "Sa kinaadman, ang palay nag-aani sa iyang kaugalingon, diin ang polen gikan sa stamen nagpabunga sa pistil sulod sa parehong bulaklak. Apan mahitabo usab ang cross-pollination.",
                "•Pollination: Sigurohon ang maayong sirkulasyon sa hangin aron mapadali ang polinasyon, ilabi na sa mga hybrid nga barayti.\n" +
                        "•Temperature Management: Panalipdan ang mga tanom gikan sa sobra nga temperatura nga makaapekto sa pagpamulak ug polinasyon.\n" +
                        "•Pest Management: Kontrolon ang mga peste nga makadaot sa mga bulak, sama sa stink bugs ug leafhoppers.", R.drawable.flowering));


        stages.add(new Stage("Pagpuno sa Butil",
                "Human sa polinasyon, ang nabuong bulaklak nag-umang sa butil. Niining yugto, ang mga butil nagapuno og starch ug uban pang sustansya, busa nagdako sila.",
                "•Nutrient Support: I-apply ang potassium aron mapalambo ang pagpuno sa lugas ug kinatibuk-ang ani.\n" +
                        "•Water Management: Padayonon ang optimal nga lebel sa tubig aron suportahan ang pagpuno sa lugas, nga hinay-hinay nga ginaminusan samtang ang tanom nagkaduol sa pagkahinog.\n" +
                        "•Disease Control: Bantayan ug pagdumala sa mga sakit sama sa bacterial leaf blight nga makaapekto sa kalidad sa lugas.", R.drawable.grain_filling));

        stages.add(new Stage("Pagkamature ug Pagkapula",
                "Padayon nga nagdako ug nagkapula ang mga butil. Ang halaman sa palay unti-unting nagakabukog ug pagkahuman sa pagkapula, magsugod na ang pagtigulang (senescence), ug ang mga tangkay ug dahon nagasugod og pagkuyaw.",
                "•Water Management: Hinay-hinay nga pagkunhod sa suplay sa tubig aron mapadali ang pagpauga ug maminusan ang risgo sa pagkahulog.\n" +
                        "•Pest Control: Panalipdan ang hinog nga mga lugas gikan sa mga peste sama sa mga langgam ug ilaga.\n" +
                        "•Harvest Timing: Anihon sa husto nga panahon kung ang mga lugas hingpit nga hinog apan dili sobra aron malikayan ang mga pagkawala.", R.drawable.maturity_and_ripening));

        stages.add(new Stage("Pag-aani",
                "Sa pagkabutang sa mga butil, handa na silang anihon.",
                "•Proper Timing: Anihon kung ang 80-90% sa mga lugas hinog na aron masiguro ang maximum nga ani ug kalidad.\n" +
                        "•Harvesting Method: Gamiton ang angay nga mga pamaagi sa pag-ani aron maminusan ang pagkawala ug kadaot sa lugas.\n" +
                        "•Post-Harvest Handling: Husto nga pagpauga sa mga lugas aron maminusan ang sulod nga kaumog ngadto sa luwas nga lebel alang sa pagtipig (mga 12-14%).", R.drawable.harvesting));


        StagesAdapter adapter = new StagesAdapter(this, stages);
        recyclerView.setAdapter(adapter);
    }
}
