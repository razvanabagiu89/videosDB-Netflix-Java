package main;

import checker.Checker;
import checker.Checkstyle;
import common.Constants;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import functions.*;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }
    /**
     * Call the main checker and the coding style checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }
        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }
    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        // entry point
        for (int i = 0; i < input.getCommands().size(); i++) {

            if (input.getCommands().get(i).getActionType().equals("command")) {
                if (input.getCommands().get(i).getType().equals("view")) {
                    CommandView.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                } else if (input.getCommands().get(i).getType().equals("favorite")) {
                    CommandFavourite.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                } else if (input.getCommands().get(i).getType().equals("rating")) {
                    CommandRating.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                }
            }

            if (input.getCommands().get(i).getActionType().equals("recommendation")) {
                if (input.getCommands().get(i).getType().equals("standard")) {
                    RecommendationStandard.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                } else if (input.getCommands().get(i).getType().equals("best_unseen")) {
                    RecommendationBestUnseen.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                } else if (input.getCommands().get(i).getType().equals("popular")) {
                    RecommendationPopular.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                } else if (input.getCommands().get(i).getType().equals("favorite")) {
                    RecommendationFavorite.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                } else if (input.getCommands().get(i).getType().equals("search")) {
                    RecommendationSearch.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                }
            }

            if (input.getCommands().get(i).getActionType().equals("query")) {
                if (input.getCommands().get(i).getObjectType().equals("actors")) {
                    if (input.getCommands().get(i).getCriteria().equals("average")) {
                        QueryActorsAverage.func(input, fileWriter, arrayResult,
                                input.getCommands().get(i), i);
                    } else if (input.getCommands().get(i).getCriteria().equals("awards")) {
                        QueryActorsAwards.func(input, fileWriter, arrayResult,
                                input.getCommands().get(i));
                    } else if (input.getCommands().get(i).
                            getCriteria().equals("filter_description")) {
                        QueryActorsFilterDescription.func(input, fileWriter, arrayResult,
                                input.getCommands().get(i));
                    }
                } else if (input.getCommands().get(i).getObjectType().equals("users")) {
                    QueryUser.func(input, fileWriter, arrayResult, input.getCommands().get(i), i);
                } else if (input.getCommands().get(i).getCriteria().equals("favorite")) {
                    QueryFavorite.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                } else if (input.getCommands().get(i).getCriteria().equals("most_viewed")) {
                    QueryMostViewed.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                } else if (input.getCommands().get(i).getCriteria().equals("longest")) {
                    QueryDuration.func(input, fileWriter, arrayResult, input.getCommands().get(i));
                } else if (input.getCommands().get(i).getCriteria().equals("ratings")) {
                    QueryRating.func(input, fileWriter, arrayResult,
                            input.getCommands().get(i), i);
                }
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}
