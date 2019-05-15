package edu.iis.mto.testreactor.exc2;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WashingMachineTest {

    WashingMachine washingMachine;
    LaundryBatch laundryBatch;
    ProgramConfiguration programConfiguration;
    Percentage percentage;
    LaundryBatch.Builder builder;
    ProgramConfiguration.Builder builder1;

    @Mock
    DirtDetector dirtDetector;

    @Mock
    Engine engine;

    @Mock
    WaterPump waterPump;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        percentage = new Percentage(0);
        builder = LaundryBatch.builder();
        builder.withWeightKg(1.0);
        builder.withType(Material.WOOL);
        laundryBatch = builder.build();


        builder1 = ProgramConfiguration.builder();
        builder1.withSpin(true);
        builder1.withProgram(Program.SHORT);
        builder.withWeightKg(1.0);
        programConfiguration = builder1.build();

        washingMachine = new WashingMachine(dirtDetector, engine, waterPump);
        when(dirtDetector.detectDirtDegree(any(LaundryBatch.class))).thenReturn(percentage);
        doNothing().when(waterPump).pour(any(Double.class));
        doNothing().when(engine).spin();
    }


    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }


    @Test
    public void testIfFunctionWithCorrectParameteresWillSuccess()
    {
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration);
        Assert.assertThat(laundryStatus.getResult(), is(Result.SUCCESS));
    }

    @Test
    public void testIfWashWithTooHeavyBatchWillBeFailure()
    {
        builder.withWeightKg(9.0);
        builder.withType(Material.WOOL);
        laundryBatch = builder.build();
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration);
        Assert.assertThat(laundryStatus.getResult(), is(Result.FAILURE));

    }

    @Test
    public void testIfDetectDirtDegreeMethodWillBeInvokedOnce()
    {
        builder1.withSpin(true);
        builder1.withProgram(Program.AUTODETECT);
        programConfiguration = builder1.build();
        washingMachine.start(laundryBatch, programConfiguration);
        verify(dirtDetector, times(1)).detectDirtDegree(any(LaundryBatch.class));

    }

    @Test (expected = IllegalArgumentException.class)
    public void testIfIllegalArgumentExceptionWillBeThrown()
    {
        builder.withWeightKg(-9.0);
    }

    @Test
    public void testIfPourMethodWillBeInvokedOnce()
    {
        washingMachine.start(laundryBatch, programConfiguration);
        verify(waterPump, times(1)).pour(any(Double.class));
    }

    @Test
    public void testIfSpinMethodWillBeInvokeOnce()
    {
        washingMachine.start(laundryBatch, programConfiguration);
        verify(engine, times(1)).spin();
    }

}
