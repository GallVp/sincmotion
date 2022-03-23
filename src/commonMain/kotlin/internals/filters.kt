package sincmotion.internals

import sincmaths.*

internal fun bandPassAt100From0d3To45(inputData: SincMatrix): SincMatrix {
    // Low pass at 45 Hz
    val b = doubleArrayOf(0.800592403464570, 1.601184806929141, 0.800592403464570)
    val a = doubleArrayOf(1.000000000000000, 1.561018075800718, 0.641351538057563)
    // High pass at 0.3 Hz
    val bb = doubleArrayOf(0.986759780439403, -1.973519560878807, 0.986759780439403)
    val aa = doubleArrayOf(1.000000000000000, -1.973344249781299, 0.973694871976315)
    return inputData.filtfilt(b, a).filtfilt(bb, aa)
}

internal fun bandPassAt100From0d1To45(inputVector: SincMatrix): SincMatrix {
    // Low pass at 45 Hz
    val b = doubleArrayOf(0.800592403464570, 1.601184806929141, 0.800592403464570)
    val a = doubleArrayOf(1.000000000000000, 1.561018075800718, 0.641351538057563)
    // High pass at 0.1 Hz
    val bb = doubleArrayOf(0.995566972017647, -1.991133944035294, 0.995566972017647)
    val aa = doubleArrayOf(1.000000000000000, -1.991114292201654, 0.991153595868935)
    return inputVector.filtfilt(b, a).filtfilt(bb, aa)
}

internal fun lowPassAt100With3(inputVector: SincMatrix): SincMatrix {
    // Low pass at 3 Hz
    val b = doubleArrayOf(0.007820208033497, 0.015640416066994, 0.007820208033497)
    val a = doubleArrayOf(1.000000000000000, -1.734725768809275, 0.766006600943264)
    return inputVector.filtfilt(b, a)
}

internal fun lowPassAt100With2(inputVector: SincMatrix): SincMatrix {
    // Low pass at 2 Hz
    val b = doubleArrayOf(0.003621681514929, 0.007243363029857, 0.003621681514929)
    val a = doubleArrayOf(1.000000000000000, -1.822694925196308, 0.837181651256023)
    return inputVector.filtfilt(b, a)
}