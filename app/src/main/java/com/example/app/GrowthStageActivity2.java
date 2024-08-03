package com.example.app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GrowthStageActivity2 extends AppCompatActivity {


    RecyclerView recylcerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_growth_stage2);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        List<Stage> stages = new ArrayList<>();
        stages.add(new Stage("Pagtubo",
                "Kini mao ang yugto diin ang buto sa mais nag-absorb og tubig gikan sa yuta ug nagsugod og pagtubo. Ang radical (gamut) mauna nga magsugod, susundan sa coleoptile (sanga).",
                "•Seed Quality: Gamita ang taas og kalidad, walay sakit nga mga binhi nga adunay taas nga rate sa pagtubo.\n" +
                        "•Seed Treatment: Trataron ang mga binhi gamit ang fungicide o insecticide aron mapanalipdan batok sa mga sakit ug peste nga gikan sa yuta.\n" +
                        "•Optimal Planting Conditions: Siguroha nga ang yuta adunay husto nga kaumog, temperatura (ideally 10-30°C), ug giladmon sa pagtanum (mga 1.5 hangtod 2 pulgada) para sa maayong pagtubo.", R.drawable.germination_corn));

        stages.add(new Stage("Yugto sa Pagpananom",
                "Sa yugto nga kini, ang tanom sa mais padayon nga naglambo sa iyang mga gamut ug sistema sa sanga. Ang unang mga dahon, nga nailhan nga coleoptile leaves, nag-abli ug nagsugod ang photosynthesis.",
                "•Soil Preparation: Siguruhon ang maayong andam sa yuta nga adunay maayong istruktura ug drainage.\n" +
                        "•Early Nutrient Management: I-apply ang starter fertilizers nga adunay daghang posporus aron suportahan ang pagtubo sa mga gamut.\n" +
                        "•Weed Control: Implementar ang early weed control aron ma-reduce ang kompetisyon alang sa nutrisyon, tubig, ug kahayag.", R.drawable.seedlingstage_corn));

        stages.add(new Stage("V6 Stage (Anim nga Dahon sa Leeg)",
                "Niining yugto, ang tanom sa mais adunay unom ka bug-os nga mga dahon nga mibuto na. Kini usa ka kritikal nga yugto alang sa pag-angkon sa nutrisyon ug paglambo sa una.",
                "•Nutrient Application: Tabangan sa abono nga nitrogen aron suportahan ang paspas nga pagtubo ug paglambo.\n" +
                        "•Pest and Disease Management: Bantayan ug kontrolon ang mga peste sama sa cutworms ug mga sakit sama sa seedling blight.\n" +
                        "•Water Management: Sigurohon ang padayon nga lebel sa kaumog aron malikayan ang stress.", R.drawable.sixcollar_corn));

        stages.add(new Stage("V12 Stage (Dose nga Dahon sa Leeg)",
                "Sa kini nga yugto, ang tanom sa mais adunay dose ka bug-os nga mga dahon nga mibuto na. Kini padayon nga naglambo og dali, ug ang paglambo sa mga gamut importante alang sa pag-angkon og kusog sa pagtukod ug pag-angkon sa nutrisyon.",
                "•Nutrient Management: Padayon ang pag-aplikar sa nitrogen kung kinahanglan ug hunahunaa ang foliar feeding nga adunay micronutrients sama sa zinc.\n" +
                        "•Irrigation: Hatagan og igong tubig, ilabi na sa panahon sa hulaw, aron suportahan ang kusog nga pagtubo.\n" +
                        "•Pest Control: Pangitaon ang mga peste sama sa corn borers ug himoon ang angay nga mga lakang sa pagkontrol.", R.drawable.leaf_corn));

        stages.add(new Stage("V18 Stage (Onse nga Dahon sa Leeg)",
                " Niining punto, ang tanom sa mais adunay onse ka bug-os nga mga dahon nga mibuto na. Kini nagkaduol na sa katapusan sa yugto sa paglambo ug nag-andam na alang sa reproduktibong paglambo.",
                "•Nutrient Support: Padayon nga balanse ang suplay sa nutrisyon, sigurohon ang pagkaanaa sa potassium ug phosphorus.\n" +
                        "•Disease Monitoring: Bantayan ang mga sakit sa dahon sama sa northern corn leaf blight ug i-aplikar ang fungicide kung kinahanglan.\n" +
                        "•Water Management: Padayon nga hatagan og igong tubig, likayan ang stress sa tubig.", R.drawable.leaf_corn));

        stages.add(new Stage("Pag-usbaw sa Tassel",
                "Kini nagtandog sa simula sa reproduktibong yugto. Ang tassel, nga naglakip sa mga anther nga nag-produce og pollen, mibuto gikan sa itaas sa tanom.",
                "•Pest and Disease Control: Panalipdan ang tanom gikan sa mga peste sa tassel ug silk sama sa corn earworms.\n" +
                        "•Water Management: Sigurohon ang igong suplay sa tubig samtang ang tanom nangandam alang sa reproduksyon.\n" +
                        "•Nutrient Management: Sigurohon nga adunay igong nitrogen ug uban pang sustansya ang tanom.", R.drawable.tassel_corn));

        stages.add(new Stage("Paglabay sa Silk",
                "Sayo human sa pag-usbaw sa tassel, ang silk mibuto gikan sa mga ear shoot nodes. Ang matag silk nagrepresentar og potensyal nga kernel, ug kini nagkapkap og pollen alang sa pagpabunga.",
                "•Pollination: Sigurohon ang maayong daloy sa hangin ug kondisyon sa palibot aron mapadali ang polinasyon.\n" +
                        "•Pest Control: Kontrolon ang mga peste sama sa Japanese beetles nga makadaot sa silk ug makaapekto sa polinasyon.\n" +
                        "•Water Management: Mantenahon ang optimal nga kaumog sa yuta aron suportahan ang paglambo sa silk ug tassel.", R.drawable.silkemergence_corn));


        stages.add(new Stage("Polinasyon",
                "Ang pollen nga gikan sa tassel nagkabug-at sa mga silk, nga nagdala sa pagpabunga sa mga ovules. Ang matag polinasyon importante alang sa paglambo sa mga kernel.",
                "•Environmental Management: Likayan ang mga kondisyon sa stress (hulaw, taas nga temperatura) panahon sa polinasyon aron masiguro ang pagbutang sa kernel.\n" +
                        "•Pest Control: Padayon nga bantayan ug kontrolon ang mga peste nga makabalda sa polinasyon.\n" +
                        "•Irrigation: Sigurohon ang padayon nga kaumog sa yuta aron suportahan ang malampuson nga polinasyon ug paglambo sa kernel.", R.drawable.pollination_corn));

        stages.add(new Stage("Pagpuno sa Lug",
                " Ang tanom sa mais nag-abot sa physiological maturity human ang mga kernel nakab-ot na ang ilang maximum nga dry weight ug ang kantidad sa kahumon nagkunhod ngadto sa optimal nga lebel alang sa pag-ani.",
                "•Nutrient Application: Ibutang ang potassium aron suportahan ang pagpuno sa kernel ug kinatibuk-ang kalidad sa lugas.\n" +
                        "•Water Management: Hatagan og igong tubig aron malikayan ang stress ug masiguro ang husto nga pagpuno sa lugas.\n" +
                        "•Disease Monitoring: Bantayan ang mga sakit sa dalunggan ug punoan ug pagdumala sumala niana.", R.drawable.grainfill_corn));

        stages.add(new Stage("Pagkahinog",
                "Sang tanom sa mais nag-abot sa physiological maturity human ang mga kernel nakab-ot na ang ilang maximum nga dry weight ug ang kantidad sa kahumon nagkunhod ngadto sa optimal nga lebel alang sa pag-ani.",
                "•Water Management: Hinay-hinay nga maminusan ang irigasyon samtang ang tanom nagkaduol sa pagkahamtong aron mapadali ang pag-uga.\n" +
                        "•Pest Control: Panalipdan ang nagkahamtong nga mga dalunggan gikan sa mga peste sama sa mga langgam ug ilaga.\n" +
                        "•Disease Control: Bantayan ang mga sakit sa ulahi nga panahon ug himoon ang kinahanglan nga mga lakang aron panalipdan ang tanom.", R.drawable.maturity_corn));

        stages.add(new Stage("Pag-ani",
                "Human nga nahinog ang mais, kini andam na sa pag-ani. Ang mga pamaagi sa pag-ani naglain-lain apan kadalasan naglakip og mekanikal nga kagamitan aron kuhaon ang mga ear gikan sa mga sanga.",
                "•Timing: Anihon sa husto nga panahon kung ang mga kernel nakab-ot na ang optimal nga kaumog (mga 20-25% alang sa mekanikal nga pag-ani).\n" +
                        "•Harvesting Method: Gamiton ang angay nga kagamitan ug teknik sa pag-ani aron maminusan ang pagkawala ug kadaot sa lugas.\n" +
                        "•Post-Harvest Handling: Ugaon ug tipigan ang lugas sa husto aron malikayan ang pagkadaot ug mapadayon ang kalidad.", R.drawable.harvest_image));

        StagesAdapter adapter = new StagesAdapter(this, stages);
        recyclerView.setAdapter(adapter);
    }
}
