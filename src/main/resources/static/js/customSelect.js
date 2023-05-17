class CustomSelect extends HTMLElement
{ 
    #selectedElement;

    constructor()
    {
        super();

        this.onclick = (e) =>
        {
            var target = e.target;
            if (target instanceof CustomOption && !target.disabled)
            {
                this.#forEachOption(opt => opt.selected = false);
                target.selected = true;

                var oldSelected = this.#selectedElement;
                this.#selectedElement = target;
                if (oldSelected != this.#selectedElement)
                {
                    this.onchange?.call();
                }
            }
        };

        $(document).ready(() => this.select(null));
    }

    get value()
    {
        return this.#selectedElement?.value || null;
    }

    get id()
    {
        return this.id;
    }

    select(value)
    {
        this.#forEachOption(option =>
        {
            var optValue = option.value;
            var isFirstFound = false;
            if (optValue == value && value !== undefined && !isFirstFound && !option.disabled)
            {
                isFirstFound = true;
                option.selected = true;

                var oldSelected = this.#selectedElement;
                this.#selectedElement = option;
                if (oldSelected != this.#selectedElement)
                {
                    this.onchange?.call();
                }
            }
            else
            {
                option.selected = false;
            }
        });
    }

    #forEachOption(action)
    {
        for (const child of this.children)
        {
            if (child instanceof CustomOption)
            {
                action(child);
            }
        }
    }

    setHiddenForOptions(data)
    {
        if (typeof data.flag != "boolean") return;

        this.#forEachOption((opt) =>
        {
            if (data?.condition)
            {
                if (data.condition(opt))
                    opt.hidden = data.flag;
                else
                    if (typeof data.orElseFlag == "boolean")
                        opt.hidden = data.orElseFlag;
            }
            else
            {
                opt.hidden = data.flag;
            }
        });
    }

    get visibleOptionCount()
    {
        var c = 0;

        this.#forEachOption((opt) =>
        {
            if (!opt.hidden) c++;
        });

        return c;
    }
}

class CustomOption extends HTMLElement
{
    #internals = null;
    #selected = false;
    #disabled = false;
    #hidden = false;

    constructor()
    {
        super();
        this.#internals = this.attachInternals();
        this.selected = false;
        this.disabled = false;
        this.hidden = false;
    }

    get value()
    {
        return this.getAttribute("value") ?? null;
    }

    get selected() 
    {
        return this.#selected ?? false;
    }

    set selected(flag)
    {
        this.#selected = this.#set(
            typeof flag == "boolean" ? flag : false,
            "selected"
        );
    }

    get disabled()
    {
        return this.#disabled;
    }

    set disabled(flag)
    {
        this.#disabled = this.#set(
            typeof flag == "boolean" ? flag : false,
            "disabled"
        );
    }

    get hidden()
    {
        return this.#hidden;
    }

    set hidden(flag)
    {
        this.#hidden = this.#set(
            typeof flag == "boolean" ? flag : false,
            "hidden"
        );
    }

    #set(flag, state)
    {
        state = `--${state}`
        if (flag)
            this.#internals.states.add(state);
        else
            this.#internals.states.delete(state);
        return flag;
    }
}

customElements.define("custom-select", CustomSelect);
customElements.define("custom-option", CustomOption);