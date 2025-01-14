package stfalcon.universalpickerdialog.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import stfalcon.universalpickerdialog.ResourceTable;
import stfalcon.universalpickerdialog.UniversalPickerDialog;
import stfalcon.universalpickerdialog.slice.data.City;
import stfalcon.universalpickerdialog.slice.data.Developer;
import stfalcon.universalpickerdialog.slice.data.SampleDataBuilder;
import java.util.ArrayList;

/**
 * MainAbilitySlice.
 */
public class MainAbilitySlice extends AbilitySlice implements UniversalPickerDialog.OnPickListener {
    private static final int KEY_SINGLE_PICK = 1;
    private static final int KEY_MULTI_PICK = 2;
    private ArrayList<City> citiesList;
    private Developer.Level[] levels;
    private Developer.Specialization[] specializations;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        citiesList = SampleDataBuilder.getCitiesList();
        levels = SampleDataBuilder.getDeveloperLevels();
        specializations = SampleDataBuilder.getDeveloperSpecializations();

        findComponentById(ResourceTable.Id_singlePickDialogButton).setClickedListener(
                c -> showCustomizedPicker(
                        "Pick A city to continue: ",
                        KEY_SINGLE_PICK,
                        new UniversalPickerDialog.Input(0, citiesList)
                )
        );

        findComponentById(ResourceTable.Id_multiPickDialogButton).setClickedListener(
                c -> showCustomizedPicker(
                        "Configure developer specs:",
                        KEY_MULTI_PICK,
                        new UniversalPickerDialog.Input(2, levels),
                        new UniversalPickerDialog.Input(0, specializations),
                        getFormattedCitiesInput()
                )
        );
        findComponentById(ResourceTable.Id_multiPickDefaultDialogButton).setClickedListener(
                c -> showDefaultPicker(
                        "Multi pick Default Style: ",
                        KEY_MULTI_PICK,
                        new UniversalPickerDialog.Input(2, levels),
                        new UniversalPickerDialog.Input(0, specializations),
                        getFormattedCitiesInput()
                )
        );
    }

    private UniversalPickerDialog.Input getFormattedCitiesInput() {
        UniversalPickerDialog.Input citiesInput =
                new UniversalPickerDialog.Input(0, citiesList);
        citiesInput.setFormatter(value -> "in" + " " + citiesList.get(value).getName());
        return citiesInput;
    }

    private void showCustomizedPicker(String title, int key, UniversalPickerDialog.Input... inputs) {
        new UniversalPickerDialog.Builder(this)
                .setTitle(title)
                .setPositiveButtonText("Ok")
                .setNegativeButtonText("Cancel")
                .setTitleColor(Color.YELLOW)
                .setPositiveButtonColor(Color.YELLOW)
                .setBackgroundColor(Color.LTGRAY)
                .setContentTextColor(Color.WHITE)
                .setContentTextSize(24)
                .setListener(this)
                .setInputs(inputs)
                .setKey(key)
                .show();
    }

    private void showDefaultPicker(String title, int key, UniversalPickerDialog.Input... inputs) {
        new UniversalPickerDialog.Builder(this)
                .setTitle(title)
                .setListener(this)
                .setInputs(inputs)
                .setKey(key)
                .show();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onPick(int[] selectedValues, int key) {
        String result;

        switch (key) {
            case KEY_SINGLE_PICK:
                result = String.format(
                        "%s (%s)",
                        citiesList.get(selectedValues[0]).getName(),
                        citiesList.get(selectedValues[0]).getCountry());
                break;
            case KEY_MULTI_PICK:
                Developer developer = new Developer(
                        levels[selectedValues[0]],
                        specializations[selectedValues[1]],
                        citiesList.get(selectedValues[2])
                );
                result = "You're looking for " + developer.toString();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + key);
        }
        new ToastDialog(getContext()).setText(result).setDuration(500).show();


    }

}
