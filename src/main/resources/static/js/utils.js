function lineCount(str)
{
    const re = new RegExp('\n', 'g');
    const l = str.match(re)?.length;
    return l ? l+1 : 1;
}

function change(value, old, valueSetter, onchange)
{
    if (value === undefined || value === null) 
        return null;

    valueSetter?.(value);
    
    if (value != old)
        onchange?.(value, old);
}