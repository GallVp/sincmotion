package sincmotion.internals

internal object NormativeModels {
    /**
     * List order: Task:Qualifier x Parameter
     */
    val modelList by lazy {
        listOf(
            listOf(FirmEOMAAR, FirmEOMAAML, FirmEOMAAAP),
            listOf(FirmECMAAR, FirmECMAAML, FirmECMAAAP),
            listOf(CompliantEOMAAR, CompliantEOMAAML, CompliantEOMAAAP),
            listOf(CompliantECMAAR, CompliantECMAAML, CompliantECMAAAP),
            listOf(
                WalkHFGaitSymmetry,
                WalkHFStepLength,
                WalkHFStepTime,
                WalkHFStepLengthVar,
                WalkHFStepTimeVar,
                WalkHFStepLengthAsym,
                WalkHFStepTimeAsym,
                WalkHFStepVelocity
            ),
            listOf(
                WalkHTGaitSymmetry,
                WalkHTStepLength,
                WalkHTStepTime,
                WalkHTStepLengthVar,
                WalkHTStepTimeVar,
                WalkHTStepLengthAsym,
                WalkHTStepTimeAsym,
                WalkHTStepVelocity
            )
        )
    }
}

internal object FirmEOMAAR : NormativeModel {
    override val intercept = 5.53288406330872
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = -0.0116628315562068
    override val sigmaBetween = 0.205664764279531
    override val sigmaTest = 0.0205396722552213
    override val sigmaWithin = 0.102584725596394

    override val sem = 0.102584725596394
    override val mdc = 0.284350352052257
    override val normativeSD = 0.230745529378566
    override val significantDigits = 1
}

internal object FirmEOMAAML : NormativeModel {
    override val intercept = 6.25253521100169
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = -0.0119198167939406
    override val sigmaBetween = 0.239943216276311
    override val sigmaTest = 0.0378474742669161
    override val sigmaWithin = 0.109638367452648

    override val sem = 0.109638367452648
    override val mdc = 0.303902049767642
    override val normativeSD = 0.266506566453976
    override val significantDigits = 1
}

internal object FirmEOMAAAP : NormativeModel {
    override val intercept = 5.28416354389839
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = -0.00655393799279049
    override val sigmaBetween = 0.166876163427827
    override val sigmaTest = 0.0
    override val sigmaWithin = 0.11377826493761

    override val sem = 0.11377826493761
    override val mdc = 0.315377260140974
    override val normativeSD = 0.201973135571551
    override val significantDigits = 1
}

internal object FirmECMAAR : NormativeModel {
    override val intercept = 5.86442368758979
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = -0.014090996881186
    override val sigmaBetween = 0.251215237778627
    override val sigmaTest = 0.0245351994930052
    override val sigmaWithin = 0.0887794978700811

    override val sem = 0.0887794978700811
    override val mdc = 0.246084213099142
    override val normativeSD = 0.26756844161522
    override val significantDigits = 2
}

internal object FirmECMAAML : NormativeModel {
    override val intercept = 6.74733760382103
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = -0.0152118526072974
    override val sigmaBetween = 0.275764066633278
    override val sigmaTest = 0.0381437665801038
    override val sigmaWithin = 0.10835180092012

    override val sem = 0.10835180092012
    override val mdc = 0.300335869282817
    override val normativeSD = 0.298732121034337
    override val significantDigits = 1
}

internal object FirmECMAAAP : NormativeModel {
    override val intercept = 5.54210438141018
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = -0.00873321295824164
    override val sigmaBetween = 0.238835884165622
    override val sigmaTest = 0.0120956144608366
    override val sigmaWithin = 0.0976385306855055

    override val sem = 0.0976385306855055
    override val mdc = 0.270640199239022
    override val normativeSD = 0.258306341634858
    override val significantDigits = 2
}

internal object CompliantEOMAAR : NormativeModel {
    override val intercept = 4.57356100342507
    override val ageInYearsBeta = -0.00575256764283847
    override val bmiBeta = 0.0
    override val heightInCMBeta = -0.0069343024659972
    override val sigmaBetween = 0.270552739420762
    override val sigmaTest = 0.0782438941752711
    override val sigmaWithin = 0.0962391122669214

    override val sem = 0.0962391122669214
    override val mdc = 0.266761209285309
    override val normativeSD = 0.297628725955199
    override val significantDigits = 2
}

internal object CompliantEOMAAML : NormativeModel {
    override val intercept = 4.73337891145681
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = -0.0050910706771259
    override val sigmaBetween = 0.276746864578503
    override val sigmaTest = 0.0903334865720866
    override val sigmaWithin = 0.123803672573614

    override val sem = 0.123803672573614
    override val mdc = 0.343166272337399
    override val normativeSD = 0.316348407919206
    override val significantDigits = 1
}

internal object CompliantEOMAAAP : NormativeModel {
    override val intercept = 4.01398090511214
    override val ageInYearsBeta = -0.00497076995133014
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.0
    override val sigmaBetween = 0.193168555298582
    override val sigmaTest = 0.053992401632453
    override val sigmaWithin = 0.0977529413685136

    override val sem = 0.0977529413685136
    override val mdc = 0.270957329472619
    override val normativeSD = 0.223125318456641
    override val significantDigits = 2
}

internal object CompliantECMAAR : NormativeModel {
    override val intercept = 2.97426162305919
    override val ageInYearsBeta = -0.00281014189854279
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.0
    override val sigmaBetween = 0.263614175487584
    override val sigmaTest = 0.0
    override val sigmaWithin = 0.161533654055361

    override val sem = 0.161533654055361
    override val mdc = 0.447748445315759
    override val normativeSD = 0.309169136413187
    override val significantDigits = 1
}

internal object CompliantECMAAML : NormativeModel {
    override val intercept = 3.64565303860477
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.0
    override val sigmaBetween = 0.28316902727674
    override val sigmaTest = 3.40813754818224e-06
    override val sigmaWithin = 0.163749791944959

    override val sem = 0.163749791944959
    override val mdc = 0.453891266144495
    override val normativeSD = 0.327106545918128
    override val significantDigits = 1
}

internal object CompliantECMAAAP : NormativeModel {
    override val intercept = 3.58269686114865
    override val ageInYearsBeta = -0.00284132695135275
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.0
    override val sigmaBetween = 0.201931908095971
    override val sigmaTest = 0.0
    override val sigmaWithin = 0.140495685562066

    override val sem = 0.140495685562066
    override val mdc = 0.389434171794488
    override val normativeSD = 0.245999051154338
    override val significantDigits = 1
}

internal object WalkHFGaitSymmetry : NormativeModel {
    override val intercept = 96.4748714362826
    override val ageInYearsBeta = 0.0
    override val bmiBeta = -0.348781791577141
    override val heightInCMBeta = -0.101139885413217
    override val sigmaBetween = 1.91283673743233
    override val sigmaTest = 0.224261946902202
    override val sigmaWithin = 1.0108165705926

    override val sem = 1.0108165705926
    override val mdc = 2.80184058627889
    override val normativeSD = 2.17508343386725
    override val significantDigits = 0
}

internal object WalkHFStepLength : NormativeModel {
    override val intercept = 0.129059491883305
    override val ageInYearsBeta = 0.000601906299518126
    override val bmiBeta = -0.00374576989301664
    override val heightInCMBeta = 0.00365547992619162
    override val sigmaBetween = 0.0385439429158307
    override val sigmaTest = 0.00357240690502139
    override val sigmaWithin = 0.0146733981097563

    override val sem = 0.0146733981097563
    override val mdc = 0.0406725844813174
    override val normativeSD = 0.0413969351363269
    override val significantDigits = 2
}

internal object WalkHFStepTime : NormativeModel {
    override val intercept = 0.245223865126362
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.00175602754033421
    override val sigmaBetween = 0.0314840395596305
    override val sigmaTest = 0.00600765609295331
    override val sigmaWithin = 0.0137544835822226

    override val sem = 0.0137544835822226
    override val mdc = 0.038125483361818
    override val normativeSD = 0.0348786825630959
    override val significantDigits = 2
}

internal object WalkHFStepLengthVar : NormativeModel {
    override val intercept = 1.72805146077874
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0515432052568402
    override val heightInCMBeta = 0.0
    override val sigmaBetween = 0.556361257218192
    override val sigmaTest = 0.11429355820073
    override val sigmaWithin = 0.595685097272104

    override val sem = 0.595685097272104
    override val mdc = 1.65115484919286
    override val normativeSD = 0.823068406082791
    override val significantDigits = 1
}

internal object WalkHFStepTimeVar : NormativeModel {
    override val intercept = 6.96383572190912
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = -0.0244077683816007
    override val sigmaBetween = 0.274196503168368
    override val sigmaTest = 0.2643904144148
    override val sigmaWithin = 0.866439646202414

    override val sem = 0.866439646202414
    override val mdc = 2.40164816932891
    override val normativeSD = 0.946469056068689
    override val significantDigits = 1
}

internal object WalkHFStepLengthAsym : NormativeModel {
    override val intercept = -6.75382553354878
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.05853588946479
    override val sigmaBetween = 2.12771207633763
    override val sigmaTest = 0.0
    override val sigmaWithin = 1.12528536056361

    override val sem = 1.12528536056361
    override val mdc = 3.11913188415994
    override val normativeSD = 2.40695363945627
    override val significantDigits = 0
}

internal object WalkHFStepTimeAsym : NormativeModel {
    override val intercept = -0.763844627433826
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.156437734354402
    override val heightInCMBeta = 0.0
    override val sigmaBetween = 1.67828832880743
    override val sigmaTest = 0.194765677609852
    override val sigmaWithin = 1.45791893470206

    override val sem = 1.45791893470206
    override val mdc = 4.04114511138052
    override val normativeSD = 2.23161667943867
    override val significantDigits = 0
}

internal object WalkHFStepVelocity : NormativeModel {
    override val intercept = 0.998269559678981
    override val ageInYearsBeta = 0.00162865372828353
    override val bmiBeta = -0.00954986969386805
    override val heightInCMBeta = 0.00256329707576291
    override val sigmaBetween = 0.127227026642836
    override val sigmaTest = 0.020196067424641
    override val sigmaWithin = 0.0508526701027674

    override val sem = 0.0508526701027674
    override val mdc = 0.140956410054748
    override val normativeSD = 0.138494012521763
    override val significantDigits = 2
}

internal object WalkHTGaitSymmetry : NormativeModel {
    override val intercept = 95.0167925023176
    override val ageInYearsBeta = -0.0676462249935574
    override val bmiBeta = -0.317378184321014
    override val heightInCMBeta = -0.0871961619880845
    override val sigmaBetween = 2.05734043087708
    override val sigmaTest = 0.522761476928826
    override val sigmaWithin = 1.03614046528618

    override val sem = 1.03614046528618
    override val mdc = 2.87203484112133
    override val normativeSD = 2.36209997123021
    override val significantDigits = 0
}

internal object WalkHTStepLength : NormativeModel {
    override val intercept = 0.0209289179720218
    override val ageInYearsBeta = 0.000653219844328837
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.00353634291003949
    override val sigmaBetween = 0.0349751985742496
    override val sigmaTest = 0.00940207158440742
    override val sigmaWithin = 0.0129864214519766

    override val sem = 0.0129864214519766
    override val mdc = 0.0359965237543933
    override val normativeSD = 0.038474804840504
    override val significantDigits = 2
}

internal object WalkHTStepTime : NormativeModel {
    override val intercept = 0.281890958390887
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.00163646782820896
    override val sigmaBetween = 0.0396300174521628
    override val sigmaTest = 0.00690798503539535
    override val sigmaWithin = 0.012215733699414

    override val sem = 0.012215733699414
    override val mdc = 0.0338602862932167
    override val normativeSD = 0.0420414401551965
    override val significantDigits = 2
}

internal object WalkHTStepLengthVar : NormativeModel {
    override val intercept = 3.47627495449032
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.0
    override val sigmaBetween = 0.381838851009413
    override val sigmaTest = 0.334621002630951
    override val sigmaWithin = 0.815058785768661

    override val sem = 0.815058785768661
    override val mdc = 2.25922769037216
    override val normativeSD = 0.960256709323405
    override val significantDigits = 1
}

internal object WalkHTStepTimeVar : NormativeModel {
    override val intercept = 2.99204697308757
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.0
    override val sigmaBetween = 0.408624571654767
    override val sigmaTest = 0.196842591340937
    override val sigmaWithin = 0.721158586402399

    override val sem = 0.721158586402399
    override val mdc = 1.99894961688368
    override val normativeSD = 0.851933537940469
    override val significantDigits = 1
}

internal object WalkHTStepLengthAsym : NormativeModel {
    override val intercept = -5.68728185372276
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.0537454025452137
    override val sigmaBetween = 2.01432393176052
    override val sigmaTest = 0.454379053424249
    override val sigmaWithin = 1.56157467312488

    override val sem = 1.56157467312488
    override val mdc = 4.32846415952742
    override val normativeSD = 2.58891419054378
    override val significantDigits = 0
}

internal object WalkHTStepTimeAsym : NormativeModel {
    override val intercept = 3.25934962796234
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.0
    override val sigmaBetween = 1.88351155390791
    override val sigmaTest = 0.0
    override val sigmaWithin = 1.50531265373865

    override val sem = 1.50531265373865
    override val mdc = 4.1725137982369
    override val normativeSD = 2.41113706769447
    override val significantDigits = 0
}

internal object WalkHTStepVelocity : NormativeModel {
    override val intercept = 0.674232944171212
    override val ageInYearsBeta = 0.0
    override val bmiBeta = 0.0
    override val heightInCMBeta = 0.00290775441581329
    override val sigmaBetween = 0.116536178919552
    override val sigmaTest = 0.0311026289843372
    override val sigmaWithin = 0.0437854253121589

    override val sem = 0.0437854253121589
    override val mdc = 0.121367006929029
    override val normativeSD = 0.128316865597137
    override val significantDigits = 2
}