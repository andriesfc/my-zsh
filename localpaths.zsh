localpaths_file="${HOME}/.localpaths.list"

if [ -f "${localpaths_file}" ]; then
    pax=""
    for pe in $(cat $localpaths_file); do
        p1=$(eval expandtilde "${pe}")
        if [[ -n "${pe}" ]]; then
            append-to-path "$p1"
        fi
    done
fi
