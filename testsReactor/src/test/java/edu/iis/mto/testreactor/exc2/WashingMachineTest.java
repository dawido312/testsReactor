package edu.iis.mto.testreactor.exc2;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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
        builder = LaundryBatch.builder();
        builder.withWeightKg(9.0);
        builder.withType(Material.WOOL);
        laundryBatch = builder.build();
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration);
        Assert.assertThat(laundryStatus.getResult(), is(Result.FAILURE));

    }

}
