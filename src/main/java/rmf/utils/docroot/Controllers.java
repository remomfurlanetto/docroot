package rmf.utils.docroot;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class Controllers {

    @GetMapping("/")
    public ModelAndView index(Model model) {

        Map<String, String> linksMapping = new HashMap<>();
        linksMapping.put("/list", "Browse");

        model.addAttribute("links", linksMapping);
        return new ModelAndView("index");

    }

    @GetMapping("/list")
    public ModelAndView list(@RequestParam (required = false) String path, Model model) {

        if(path == null || path.equals("")) {
            return new ModelAndView("redirect:/list?path=" + OSUtils.getDefaultMountedPoint());
        }

        if(!path.endsWith("/")){
            path = path + "/";
        }

        File directory = new File(path);

        List<String> resultDirectoriesList = new ArrayList<String>();
        List<String> resultFilesList = new ArrayList<String>();

        if(directory.canRead()) {
            resultDirectoriesList.addAll(listDirectoriesUsingJavaIO(directory));
            resultFilesList.addAll(listFilesUsingJavaIO(directory));
        } else {
            model.addAttribute("errorMessage", "You don't have permissions to read this directory");
        }

        model.addAttribute("pathParam", path);
        model.addAttribute("pathParamFiles", path.replaceFirst("/", "/disk/"));
        model.addAttribute("breadcrumb", getBreadcrumb(path));
        model.addAttribute("separator", "/");
        model.addAttribute("directories", resultDirectoriesList);
        model.addAttribute("files", resultFilesList);

        return new ModelAndView("printList");
    }

    private Set<String> listDirectoriesUsingJavaIO(File directory) {

        if(directory.listFiles() == null){
            return Collections.emptySet();
        }

        return Stream.of(directory.listFiles())
                .filter(file -> !file.isFile())
                .filter(file -> file.canRead())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    private Set<String> listFilesUsingJavaIO(File directory) {

        if(directory.listFiles() == null){
            return Collections.emptySet();
        }

        return Stream.of(directory.listFiles())
                .filter(file -> !file.isDirectory())
                .filter(file -> file.canRead())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    private TreeMap<String, String> getBreadcrumb(String path){
        TreeMap<String, String> map = new TreeMap<>();
        String[] parts = path.split("/");

        String link;
        if(OSUtils.isWindowsFileSeparator()){
            link = "";
        } else {
            link = "/";
        }

        for(int i=0; i<parts.length; i++){
            if(OSUtils.isWindowsFileSeparator()){
                link = link == null ? link + "/" + parts[i] : parts[i];
            } else {
                link = !link.equals("/") ? link + "/" + parts[i] : parts[i];
            }

            map.put(link, parts[i]);
        }

        return map;
    }
}