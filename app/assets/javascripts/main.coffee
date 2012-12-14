class window.Choice
  constructor: (@row, @col, @option) ->
    @id = "##{@row}#{@col}-#{@option}"
    @cellId = "##{@row}#{@col}"
    @rowId = "#row#{@row}"
    @pick = "p#{@option}"
    @rowPick = "#{@row}-#{@pick}"

dismissedChoices = {}
picked = {}

# route pick/unpick actions taken on possible choices and chosen answers
routePick = (c) ->
  if dismissedChoices[c.id]
    undismissChoice c
  else if picked[c.rowPick]
    dupeAnswerPicked(c)
  else pickAnswer(c)
  false

routeUnpick = (c) ->
  if picked[c.rowPick] == c.cellId
      unpickAnswer(c)
  else if picked[c.rowPick]
    dupeDismiss(c)
  else if dismissedChoices[c.id]
    undismissChoice c
  else
    dismissChoice c
  false

# pick a possible choice as an answer / undo a previous answer pick
pickAnswer = (c) ->
  if checkClues(c)
    doAnswer c

doAnswer = (c) ->
  console.log(c.id + ": answer")
  picked[c.rowPick] = c.cellId
  $(c.rowId).addClass(c.pick).find('.cell.unpicked').each (x) -> checkForced $ this
  $(c.cellId).removeClass("unpicked").addClass("picked "+c.pick)

unpickAnswer = (c) ->
  unless prepickedAnswer(c)
    console.log(c.id + ": unanswer")
    picked[c.rowPick] = undefined
    $(c.cellId).addClass("unpicked").removeClass("picked "+c.pick)
    $(c.rowId).removeClass(c.pick)
  else
    refuse $ c.cellId

dupeAnswerPicked = (c) ->
  console.log("already picked a #{c.pick} for row #{c.row}")
  highlightPicked(c)

# Dismiss/undismiss possible choices
dismissChoice = (c) ->
  console.log(c.id + ": dismiss")
  dismissedChoices[c.id] = true
  $(c.id).addClass "dismissed"
  checkForced $ c.cellId
undismissChoice = (c) ->
  console.log(c.id + ": undismiss")
  dismissedChoices[c.id] = false
  $(c.id).removeClass "dismissed"
dupeDismiss = (c) ->
  console.log("choice #{c.id} already dismissed by picked answer: #{picked[c.rowPick]}")
  highlightPicked(c)

highlightPicked = (c) ->
  refuse $(c.rowId+" .picked."+c.pick + "." + c.pick)

refuse = (el) -> el.effect("bounce")

checkForced = (cell) ->
  row = cell.parent()
  remaining = cell.find('.choice:not(.dismissed)').filter((x) -> !row.hasClass($(this).data('pick-class')))
  if remaining.length == 1
    #doAnswer getChoice $ remaining[0]
    console.log("want to auto-answer:", getChoice $ remaining[0])

checkClues = (c) -> true


prepickedAnswer = (c) ->
  $(c.cellId).is('.locked')

toggleDismissClue = (c) ->
  if $(c).hasClass "dismissed"
    $(c).removeClass "dismissed"
  else
    $(c).addClass "dismissed"
  false

getChoice = (p) ->
  new Choice(p.parent().data('row'), p.parent().data('col'), p.data('pick'))

jQuery ->
  if Modernizr.touch
    $("body").addClass("touch")
    $(document).bind('touchmove', false)

    $(".choice")
      .hammer()
      .bind('tap', () -> routeUnpick getChoice $ this)
      .bind('hold', () -> routePick getChoice $ this)

    $(".clue").hammer().bind 'swipe', () -> toggleDismissClue $ this
  else
    $("body").addClass("desktop")
    $(".choice")
      .bind('contextmenu', () -> routeUnpick getChoice $ this)
      .bind('click', () -> routePick getChoice $ this)

    $(".clue").hammer().bind 'contextmenu', () -> toggleDismissClue $ this


  for choice in initialAnswers
    do (choice) -> doAnswer(choice).addClass('locked')

