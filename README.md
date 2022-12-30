<div align="center">
    <h1>File Merger</h1>
    <p>CLI tool for merging files considering dependencies</p>
    <img src="https://cdn0.iconfinder.com/data/icons/file-58/512/merge-file-document-1024.png" height="300" alt="Logo">
</div>


### Restrictions

In order for this tool to find the dependencies, the files must contain a directive with the following format:

    require ‘relative/path/from/root’

In case of a circular dependency, the tool will print an error and exit.

In case of a missing dependency, the tool will print a warning and continue.

### Usage

1. Run the application.
2. Enter the path to the root directory. _(Can be relative or absolute)_
3. Enter the path to the output file. _(Can be relative or absolute)_

### Example

To test the tool, you can use `example/` folder. It should work without any errors and warnings.

1. Run the application.
2. Type `example`
3. Press enter.
4. Type `output.txt`
5. Press enter.

The tool will print a mering order and create a file `output.txt` in the root directory with the merged content.