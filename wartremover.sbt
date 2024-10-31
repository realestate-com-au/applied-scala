Compile / compile / wartremoverErrors ++= Warts.allBut(
  Wart.Nothing,
  Wart.Any,
  Wart.FinalCaseClass,
  Wart.Overloading,
  Wart.LeakingSealed,
  Wart.NonUnitStatements,
  Wart.Option2Iterable,
  Wart.EitherProjectionPartial,
  Wart.TripleQuestionMark,
)
