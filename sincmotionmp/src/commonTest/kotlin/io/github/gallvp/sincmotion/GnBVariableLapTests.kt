package io.github.gallvp.sincmotion

import io.github.gallvp.sincmaths.SincMatrix
import io.github.gallvp.sincmaths.asRowMajorArray
import io.github.gallvp.sincmaths.asSincMatrix
import io.github.gallvp.sincmaths.cat
import io.github.gallvp.sincmaths.csvRead
import io.github.gallvp.sincmaths.get
import io.github.gallvp.sincmaths.getCol
import io.github.gallvp.sincmaths.getCols
import io.github.gallvp.sincmaths.numRows
import io.github.gallvp.sincmaths.numel
import io.github.gallvp.sincmaths.plus
import io.github.gallvp.sincmotion.gaitandbalance.estimateGnBGaitOutcomes
import io.github.gallvp.sincmotion.gaitandbalance.segmentGnBGaitStream
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GnBVariableLapTests {
    @Test
    fun splitsEveryLapWithoutDroppingOrDuplicatingSamples() {
        for (lapCount in listOf(1, 4, 6, 8)) {
            val samples = lapCount * 3
            val time = DoubleArray(samples) { i -> (i / 3) * 4.0 + (i % 3) * 0.01 }.asSincMatrix(false)
            val accel = DoubleArray(samples * 3) { it.toDouble() }.asSincMatrix(samples, 3)
            val rot = DoubleArray(samples * 4) { (it + 100).toDouble() }.asSincMatrix(samples, 4)
            val gyro = DoubleArray(samples * 3) { (it + 200).toDouble() }.asSincMatrix(samples, 3)
            val segments = segmentGnBGaitStream(time, accel, rot, gyro)
            for ((original, laps) in listOf(
                accel to segments.accelSegments,
                rot to segments.rotSegments,
                gyro to segments.gyroSegments,
            )) {
                assertEquals(lapCount, laps.size)
                assertTrue(laps.all { it.numRows == 3 })
                assertContentEquals(original.asRowMajorArray(), laps.reduce { a, b -> a.cat(1, b) }.asRowMajorArray())
            }
        }
    }

    @Test
    fun keepsTheExistingStrictOneSecondPauseThreshold() {
        val time = doubleArrayOf(0.0, 1.0, 2.01).asSincMatrix(false)
        val sensor = doubleArrayOf(10.0, 20.0, 30.0).asSincMatrix(false)
        val laps = segmentGnBGaitStream(time, sensor, sensor, sensor).accelSegments
        assertEquals(listOf(2, 1), laps.map { it.numRows })
        assertContentEquals(doubleArrayOf(10.0, 20.0), laps[0].asRowMajorArray())
        assertContentEquals(doubleArrayOf(30.0), laps[1].asRowMajorArray())
    }

    @Test
    fun rejectsMismatchedSensorStreams() {
        val time = doubleArrayOf(0.0, 0.01).asSincMatrix(false)
        val shortStream = doubleArrayOf(1.0).asSincMatrix(false)
        assertFailsWith<IllegalArgumentException> {
            segmentGnBGaitStream(time, time, shortStream, time)
        }
    }

    @Test
    fun preservesPublishedFourLapExampleOutcomes() {
        GnBExampleCases().evaluateAll(1.0E-10)
    }

    @Test
    fun analyzesEightLapsOfWalkingData() {
        val examples = GnBExampleCases()
        val name = examples.exampleNames.first()
        val height = examples.exampleParticipantHeight.first()
        val data =
            SincMatrix.csvRead(
                filePath = "example_data/gaitandbalance/$name.csv",
                separator = ",",
                headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d"),
            )
        val time = data.getCol(1)
        val accel = data.getCols(intArrayOf(2, 3, 4))
        val gyro = data.getCols(intArrayOf(5, 6, 7))
        val rot = data.getCols(intArrayOf(8, 9, 10, 11))
        val offset = time[time.numel] - time[1] + 3.0
        val extendedTime = time.cat(1, time + offset)
        val extendedAccel = accel.cat(1, accel)
        val extendedRot = rot.cat(1, rot)
        val extendedGyro = gyro.cat(1, gyro)
        assertEquals(8, segmentGnBGaitStream(extendedTime, extendedAccel, extendedRot, extendedGyro).accelSegments.size)
        val four = estimateGnBGaitOutcomes(time, accel, rot, gyro, 100.0, height)
        val eight = estimateGnBGaitOutcomes(extendedTime, extendedAccel, extendedRot, extendedGyro, 100.0, height)
        assertEquals(four.meanStepLength, eight.meanStepLength, 1.0E-10)
        assertEquals(four.meanStepTime, eight.meanStepTime, 1.0E-10)
        assertEquals(four.meanSymIndex, eight.meanSymIndex, 1.0E-10)
        assertEquals(four.meanStepVelocity, eight.meanStepVelocity, 1.0E-10)
        assertTrue(eight.stepLengthVariability.isFinite())
        assertTrue(eight.stepTimeVariability.isFinite())
    }
}
