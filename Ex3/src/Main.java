import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    // The signature doesn't match exactly what was specified in the assignment document but I wanted to add some simple metrics,
    // and I additionally wasn't able to think of a way to solve it without passing the modified list back to itself.
    // It especially makes edge cases difficult.
    // Because Java passes by reference it's very simple to return the accumulated list when the search finishes,
    // and to pass the accumulated list to any nested calls to tally up any files within sub-directories.
    // as well as to initialize and check edge cases. For instance, if found is empty, we can just create it.
    // Being able to return and pass around and initialize before use is very powerful.
    public static List<String> find(List<String> found, AtomicInteger scancount, File path, String filename)
    {
        // base cases

        // don't dereference null ptrs
        if (found == null)
            found = new ArrayList<>(0);

        if (path == null)
            return found;

        if(path.list() == null)
            return found;

        // ensure any user-input is actually a valid file-system entry
        if (!path.exists())
            return found;

        // path must be a directory.
        assert path.isDirectory();

        // because we start from a dir, expand the list of filenames
        for (String childName : path.list()) {
            // construct temporary file to scan file or dir from path
            File child = new File(path, childName);
            scancount.incrementAndGet();

            // if the scanned folder is a directory, enter it
            // when the scanned folder and any sub-directories are scanned, this will return back to the root
            if(child.isDirectory())
                find(found, scancount, child, filename);

            // catch all non-directories in our subdirectory scan.
            // if the filenames match, add it to our accumulator list in-place.
            else if(child.getName().equals(filename)) {
                found.add(child.getAbsolutePath());
            }
        }

        // directory scan is complete, return paths of all found matches
        return found;
    }

    // Search + benchmark results on my Ryzen 7 3700x:
    //    /home/ashley/barrett/build/_deps/adlmidi-subbuild/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/android/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/examples/sdl2_mixer/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/examples/sdl2_audio/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/test/conversion/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/test/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/test/bankmap/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/test/wopl-file/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/utils/xmi2mid/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/utils/adlmidi-2/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/utils/dumpbank/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/utils/gen_adldata/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/utils/winmm_drv/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/utils/midiplay/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/utils/dumpmiles/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/utils/vlc_codec/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/adlmidi-src/utils/mus2mid/CMakeLists.txt
    //    /home/ashley/barrett/build/_deps/inih-subbuild/CMakeLists.txt
    //    /home/ashley/barrett/CMakeLists.txt
    //    3784 files scanned in 31ms.
    public static void main(String[] args) {
        // some fun metrics to print out

        // kind of nasty workaround because I want to measure how many times the loop runs with a perf counter
        // primitives are passed by value... so I can't just keep daisy-chaining it back into the recursive function
        // as per the docs... "An AtomicInteger is used in applications such as atomically incremented counters",
        // so it is a good fit despite being somewhat nasty... I wish I could just do &scancount with an int.
        // This gets passed-by-reference and just uses simple getters/setters so is simple and the intended use.
        // https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicInteger.html
        AtomicInteger scancount = new AtomicInteger(0);

        long start = System.nanoTime();

        // scan for all CMakeLists from dependencies and sub-dependencies in a C project of mine
        // creating the arraylist in place because List<T> is abstract thus can't be used like this, but this works good
        // this prevents null pointer references when dealing with passing the initially-empty buffer to itself to be filled.
        List<String> outBuffer = find(new ArrayList<>(0), scancount, new File("/home/ashley/barrett"), "CMakeLists.txt");

        // 1 ms = 1 million ns
        long elapsed = (System.nanoTime() - start) / 1000000;

        for(String prt : outBuffer)
        {
            System.out.println(prt);
        }

        System.out.printf(scancount.get() + " files scanned in %dms.", elapsed);
    }
}