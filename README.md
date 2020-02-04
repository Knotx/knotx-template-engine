[![Build Status](https://dev.azure.com/knotx/Knotx/_apis/build/status/Knotx.knotx-template-engine?branchName=master)](https://dev.azure.com/knotx/Knotx/_build/latest?definitionId=12&branchName=master)
[![CodeFactor](https://www.codefactor.io/repository/github/knotx/knotx-template-engine/badge)](https://www.codefactor.io/repository/github/knotx/knotx-template-engine)
[![codecov](https://codecov.io/gh/Knotx/knotx-template-engine/branch/master/graph/badge.svg)](https://codecov.io/gh/Knotx/knotx-template-engine)

# Template Engine
Knot.x Template Engine module is a [Knot](https://github.com/Knotx/knotx-fragments/tree/master/api#knot)
responsible for processing [Fragment's](https://github.com/Knotx/knotx-fragments/tree/master/api#knotx-fragment-api)
`body` (treating it as a Template) and the data from Fragment's `payload` using chosen
template engine strategy.

## How does it work
Template Engine reads [Fragment's](https://github.com/Knotx/knotx-fragments/tree/master/api#knotx-fragment-api)
`body` and treats it as a Template. It also reads Fragment's `payload` and uses the data within it
to resolve placeholders from the Template. Finally it overwrites Fragment's `body` and returns it
in the [`FragmentResult`](https://github.com/Knotx/knotx-fragments/blob/master/handler/api/docs/asciidoc/dataobjects.adoc#FragmentResult)
together with Transition.

> Please note that example below uses Handlebars to process the markup. Read more about it below.
> You may read about it in the [Handlebars module docs](https://github.com/Knotx/knotx-template-engine/tree/master/handlebars)

*Fragment's body*
```html
  <div class="col-md-4">
    <h2>{{_result.title}}</h2>
    <p>{{_result.synopsis.short}}</p>
    <div>Success! Status code: {{_response.statusCode}}</div>
  </div>
```

*Fragment's payload*
```json
{
  "_result": {
    "title":"Knot.x in Action",
    "synopsis": {
      "short": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vel enim ac augue egestas rutrum non eget libero."
    }
  },
  "_response": {
    "statusCode":"200"
  }
}
```

*Fragment's body after processing*
```html
<div class="col-md-4">
  <h2>Knot.x in Action</h2>
  <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vel enim ac augue egestas rutrum non eget libero.</p>
  <div>Success! Status code: 200</div>
</div>
```

## How to use
> Please note that example below uses Handlebars to process the markup. Read more about it below.
> You may read about it in the [Handlebars module docs](https://github.com/Knotx/knotx-template-engine/tree/master/handlebars).
> For using a different template engine, refer to [pebble module docs]() or [core module docs](https://github.com/Knotx/knotx-template-engine/tree/master/core).

Define a module that creates `io.knotx.te.core.TemplateEngineKnot` instance.

```hocon
modules {
  myTemplateEngine = "io.knotx.te.core.TemplateEngineKnot"
}
```

Configure it to listen on some `address` and other things:
```hocon
config.myTemplateEngine {
  address = my.template.engine.eventbus.address
  factory = handlebars
}
```
See the [configuration docs](https://github.com/Knotx/knotx-template-engine/blob/master/core/docs/asciidoc/dataobjects.adoc)
for detailed configuration options.

In the [Fragment's Handler actions section](https://github.com/Knotx/knotx-fragments/tree/master/handler/core#actions) 
define a Template Engine Knot Action using `knot` factory.
```hocon
actions {
  te-hbs {
    factory = knot
    config {
      address = my.template.engine.eventbus.address
    }
  }
}
```

Now you may use it in Fragment's Tasks.

Example configuration is available in the [conf](https://github.com/Knotx/knotx-template-engine/blob/master/conf)
section of this module.

## OOTB Template Engine Strategies
Currently this repository delivers `handlebars` and `pebble` TE strategies implementation.
You may read more in the [handlebars](https://github.com/Knotx/knotx-template-engine/tree/master/handlebars) and [pebble](https://github.com/Knotx/knotx-template-engine/tree/master/pebble) module docs.


### How to create a custom Template Engine Strategy
Read more about it in the [API module docs](https://github.com/Knotx/knotx-template-engine/tree/master/api).
