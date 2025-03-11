package com.example.hseshellfinanceapp.ui.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

public class ShellMenu {
    private final String title;
    private final List<MenuOption> options = new ArrayList<>();

    public ShellMenu(String title) {
        this.title = title;
    }

    public void addOption(MenuOption option) {
        options.add(option);
    }

    public String getTitle() {
        return title;
    }

    public List<MenuOption> getOptions() {
        return options;
    }

    public MenuOption findOptionByCommand(String command) {
        return options.stream()
                .filter(option -> option.getCommand().equals(command))
                .findFirst()
                .orElse(null);
    }

    public String render() {
        String[][] data = new String[options.size() + 1][2];
        data[0] = new String[]{"Command", "Description"};

        for (int i = 0; i < options.size(); i++) {
            MenuOption option = options.get(i);
            data[i + 1] = new String[]{option.getCommand(), option.getDescription()};
        }
        
        ArrayTableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);

        return title.toUpperCase() + "\n" + tableBuilder.build().render(80);
    }
}
