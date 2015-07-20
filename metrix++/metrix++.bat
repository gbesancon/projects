@echo OFF

echo ^< -------------------- Executing %0 -------------------- ^>

set METRIXPP_DIR=%cd%
echo METRIXPP_DIR = %METRIXPP_DIR%

set PROJECT_DIR=%~1
if "%~1"=="" set PROJECT_DIR=%cd%\
echo PROJECT_DIR = %PROJECT_DIR%

pushd %PROJECT_DIR%
echo Collect information
python "%METRIXPP_DIR%\metrix++.py" collect^
 --std.general.size^
 --std.code.length.total^
 --std.code.lines.total^
 --std.code.lines.code^
 --std.code.lines.preprocessor^
 --std.code.lines.comments^
 --std.code.complexity.cyclomatic^
 --std.code.complexity.maxindent^
 --std.code.magic.numbers^
 --std.code.todo.comments^
 --std.code.todo.strings^
 --std.suppress^
 --std.general.procerrors^
 --std.general.proctime
echo Collect information: DONE

echo View information
python "%METRIXPP_DIR%\metrix++.py" view
echo View information: DONE
popd