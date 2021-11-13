
function date() {
  if [ "$1" = "-I" ]; then
    command date "+%Y-%m-%dT%H:%M:%S%z"
  else
   command date "$@"
  fi
}


function append_to_path() {
  export PATH="$PATH:$1"
}

function current_dir_name()
{
    echo $(python3 -c "from pathlib import Path; print(Path('.').absolute().name)")
}

function lst() {
    exa -l -h --group-directories-first -U -F --accessed --time-style long-iso
}

# Some interesting alias taken from https://www.thorsten-hans.com/5-types-of-zsh-aliases

# open any file with the following extensions usig vscode
alias -s {md,json,yaml,yml,xml,conf,ini}=code

