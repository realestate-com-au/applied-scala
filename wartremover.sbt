wartremoverErrors in (Compile, compile) ++= Warts.allBut(
  Wart.Nothing,
  Wart.Any,
  Wart.Recursion,
  Wart.FinalCaseClass,
  Wart.Overloading,
  Wart.LeakingSealed,
  Wart.NonUnitStatements,
)
